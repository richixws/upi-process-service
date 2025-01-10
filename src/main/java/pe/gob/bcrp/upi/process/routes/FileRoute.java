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

    public FileRoute(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @Value("${source.location}")
    private String sourceLocation;

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
                .to("file://".concat(targetErrorLocation));

        getContext().getRegistry().bind("fileSorter", new FileSorter<Object>());

        from(buildPathUrl())
                .routeId("file-sync")
                .autoStartup(routeAutostart)
                .routePolicy(startPolicy)
                .log(LoggingLevel.INFO, "${file:name}")
                .process(exchange -> {
                    String newBody = exchange.getIn().getBody(String.class).replaceAll("\\uFEFF", "");
                    exchange.getMessage().setBody((newBody));

                    exchange.getIn().setHeader("FileAddedDate", java.time.LocalDateTime.now());

                })
                .choice()
                .when(header("CamelFileName").contains("orders"))
                .log(LoggingLevel.INFO, "Order file")
                .to("direct:orderRoute");

        from("direct:orderRoute")
                .log(LoggingLevel.INFO, "${body}")
                .unmarshal(orderCsvDataFormat)
                .split(body(), new ListAggrStrategy())
                .streaming()
                .shareUnitOfWork()
                //.bean(transferenciaService, "persistTransferencia")
                .process(exchange -> {

                    Date fileCreationDate = new Date(exchange.getIn().getHeader("CamelFileLastModified", Long.class));
                    // Fecha de procesamiento
                    LocalDateTime processingDate = LocalDateTime.now();
                    // Obtener los datos del archivo del intercambio
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    String filePath = exchange.getIn().getHeader("CamelFileAbsolutePath", String.class);
                    Integer fileSize = exchange.getIn().getHeader("CamelFileLength", Integer.class);
                    String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
                    String mime = Files.probeContentType(Paths.get(filePath));
                    // Imprimir para verificar los datos
                    System.out.println("MIME Type: " + mime);
                    System.out.println("File Extension: " + extension);
                    System.out.println("File Name: " + fileName);
                    System.out.println("File Path: " + filePath);
                    System.out.println("File Size: " + fileSize);
                    System.out.println("File Creation Date: " + fileCreationDate);

                    FileDTO file=new FileDTO();
                    file.setFilename(fileName);
                    file.setPath(filePath);
                    file.setSize(fileSize);
                    file.setExtension(extension);
                    file.setMime(mime);

                    // Fecha de adición
                    LocalDateTime addedDate = exchange.getIn().getHeader("FileAddedDate", LocalDateTime.class);
                    file.setCreateDateFile(Fecha.formatDateTime(addedDate));
                    file.setProcessDateFile(Fecha.formatDateTime(processingDate));

                    TransferenciaCsvRecord transferenciaCsvRecord = exchange.getIn().getBody(TransferenciaCsvRecord.class);
                    transferenciaService.persistTransferencia(transferenciaCsvRecord, file);
                })
                .marshal(orderCsvDataFormat)
                .log(LoggingLevel.INFO, "${body}")
                .log(LoggingLevel.INFO, "${file:name}")
                .to("file://".concat(targetLocation));
    }


    private String buildPathUrl() {
        StringBuilder stringBuilder = new StringBuilder("file://");
        stringBuilder.append(sourceLocation)
                .append("?delete=false&sorter=#fileSorter");

        return stringBuilder.toString();

    }



}
