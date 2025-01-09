package pe.gob.bcrp.upi.process.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.dataformat.CsvDataFormat;
import org.apache.camel.routepolicy.quartz.CronScheduledRoutePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.gob.bcrp.upi.process.models.entity.Transferencia;
import pe.gob.bcrp.upi.process.models.entity.TransferenciaCsvRecord;
import pe.gob.bcrp.upi.process.service.TransferenciaService;
import pe.gob.bcrp.upi.process.util.FileSorter;
import pe.gob.bcrp.upi.process.util.ListAggrStrategy;

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

                    // Obtener datos del archivo
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    String filePath = exchange.getIn().getHeader("CamelFileAbsolutePath", String.class);
                    Long fileSize = exchange.getIn().getHeader("CamelFileLength", Long.class);
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
                .bean(transferenciaService, "persistTransferencia")
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
