package pe.gob.bcrp.upi.process.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Fecha {


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    /**
     * Convierte un LocalDate a un String en formato dd-MM-yyyy.
     *
     * @param date la fecha a formatear
     * @return la fecha formateada como String
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(FORMATTER);
    }

    /**
     * Convierte un String en formato dd-MM-yyyy a un LocalDate.
     *
     * @param dateString la fecha en formato String
     * @return la fecha como LocalDate
     */
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, FORMATTER);
    }

    /**
     * Convierte un LocalDateTime a un String en formato dd--MM-yyyy HH:mm:ss.
     *
     * @param dateTime la fecha y hora a formatear
     * @return la fecha formateada como String
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Convierte un String en formato dd--MM-yyyy HH:mm:ss a un LocalDateTime.
     *
     * @param dateTimeString la fecha y hora en formato String
     * @return la fecha y hora como LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

}
