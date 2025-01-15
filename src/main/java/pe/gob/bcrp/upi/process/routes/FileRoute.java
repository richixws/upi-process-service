package pe.gob.bcrp.upi.process.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.routepolicy.quartz.CronScheduledRoutePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.gob.bcrp.upi.process.models.dto.FileDTO;
import pe.gob.bcrp.upi.process.models.entity.File;
import pe.gob.bcrp.upi.process.models.entity.Transferencia;
import pe.gob.bcrp.upi.process.models.entity.TransferenciaCsvRecord;
import pe.gob.bcrp.upi.process.service.TransferenciaService;
import pe.gob.bcrp.upi.process.util.Fecha;
import pe.gob.bcrp.upi.process.util.FileSorter;
import pe.gob.bcrp.upi.process.util.ListAggrStrategy;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class FileRoute extends RouteBuilder {

    private static final String AMPERSAND = "&";
    private final BindyCsvDataFormat orderCsvDataFormat = new BindyCsvDataFormat(TransferenciaCsvRecord.class);
    private final TransferenciaService transferenciaService;


    @Value("${sftp.host}")
    private String sftpHost;

    @Value("${sftp.port}")
    private String sftpPort;

    @Value("${sftp.username}")
    private String sftpUsername;

    @Value("${sftp.password}")
    private String sftpPassword;

    @Value("${sftp.remote.directory}")
    private String remoteDirectory;

    @Value("${target.location}")
    private String targetLocation;

    @Value("${target.error.location}")
    private String targetErrorLocation;

    @Value("${cron.start}")
    private String cronStart;

    @Value("${cron.stop}")
    private String cronStop;

    @Value("${route.autostart}")
    private String routeAutostart;


    public FileRoute(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @Override
    public void configure() throws Exception {

        CsvDataFormat csv = new CsvDataFormat();
        csv.setDelimiter(";");
        csv.setUseMaps("true");

        CronScheduledRoutePolicy startPolicy = new CronScheduledRoutePolicy();
        startPolicy.setRouteStartTime(cronStart);
        startPolicy.setRouteStopTime(cronStop);

        onException(Exception.class)
                .maximumRedeliveries(1)
                .handled(true)
                .log(LoggingLevel.INFO, "${file:name}")
                .log("Exception occurred due: ${exception}")
                .useOriginalMessage()
                .to("file://" + targetErrorLocation);

        getContext().getRegistry().bind("fileSorter", new FileSorter<Object>());

        // Main SFTP route
        from(buildSftpUrl())
                .routeId("sftp-sync")
                .autoStartup(routeAutostart)
                .routePolicy(startPolicy)
                .log(LoggingLevel.INFO, "Processing file: ${file:name}")
                .process(exchange -> {
                    String newBody = exchange.getIn().getBody(String.class).replaceAll("\\uFEFF", "");
                    exchange.getMessage().setBody(newBody);
                    exchange.getIn().setHeader("FileAddedDate", LocalDateTime.now());
                })
                .choice()
                .when(header("CamelFileName").contains("orders"))
                .log(LoggingLevel.INFO, "Processing order file")
                .to("direct:orderRoute")
                .end();

        from("direct:orderRoute")
                .log(LoggingLevel.INFO, "${body}")
                .unmarshal(orderCsvDataFormat)
                .split(body(), new ListAggrStrategy())
                .streaming()
                .shareUnitOfWork()
                .process(exchange -> {
                    Date fileCreationDate = new Date(exchange.getIn().getHeader("CamelFileLastModified", Long.class));
                    LocalDateTime processingDate = LocalDateTime.now();
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    String filePath = exchange.getIn().getHeader("CamelFileAbsolutePath", String.class);
                    Integer fileSize = exchange.getIn().getHeader("CamelFileLength", Integer.class);
                    String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                    String mime = Files.probeContentType(Paths.get(filePath));

                    FileDTO file = new FileDTO();
                    file.setFilename(fileName);
                    file.setPath(filePath);
                    file.setSize(fileSize);
                    file.setExtension(extension);
                    file.setMime(mime);

                    LocalDateTime addedDate = exchange.getIn().getHeader("FileAddedDate", LocalDateTime.class);
                    file.setCreateDateFile(Fecha.formatDateTime(addedDate));
                    file.setProcessDateFile(Fecha.formatDateTime(processingDate));

                    TransferenciaCsvRecord transferenciaCsvRecord = exchange.getIn().getBody(TransferenciaCsvRecord.class);
                    transferenciaService.persistTransferencia(transferenciaCsvRecord, file);
                })
                .marshal(orderCsvDataFormat)
                .log(LoggingLevel.INFO, "Processed file content: ${body}")
                .to("file://" + targetLocation);
    }


    private String buildSftpUrl() {
        StringBuilder builder = new StringBuilder("sftp://")
                .append(sftpUsername)
                .append("@")
                .append(sftpHost)
                .append(":")
                .append(sftpPort)
                .append(remoteDirectory)
                .append("?password=")
                .append(sftpPassword)
                .append(AMPERSAND)
                .append("delete=true")
                .append(AMPERSAND)
                .append("disconnectOnBatchComplete=true")
                .append(AMPERSAND)
                .append("stepwise=false")
                .append(AMPERSAND)
                .append("sorter=#fileSorter");

        return builder.toString();
    }



}
