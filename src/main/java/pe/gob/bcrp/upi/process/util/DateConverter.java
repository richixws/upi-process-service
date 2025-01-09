package pe.gob.bcrp.upi.process.util;

import org.apache.camel.dataformat.bindy.Format;
import org.apache.camel.dataformat.bindy.format.FormatException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {


    public static class CustomConverter implements Format<Date> {

        @Override
        public String format(Date object) throws Exception {
            DateFormat format = getDateFormat("dd/MM/yyyy HH:mm");

            return format.format(object);
        }

        @Override
        public Date parse(String string) throws Exception {


            // Intentar convertir en formato dd/MM/yyyy HH:mm
            DateFormat format = getDateFormat("dd/MM/yyyy HH:mm");
            try {
                return format.parse(string);
            } catch (Exception ex) {
                try {
                    // Intentar convertir en formato yyyy-MM-dd HH:mm:ss
                    DateFormat oformat = getDateFormat("yyyy-MM-dd HH:mm:ss");
                    return oformat.parse(string);
                } catch (Exception e) {
                    try {
                        // Intentar convertir en formato ISO-8601
                        SimpleDateFormat isoFormat = getDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Asegurar UTC
                        return isoFormat.parse(string);
                    } catch (Exception isoEx) {
                        throw new FormatException("Date provided does not fit dd/MM/yyyy HH:mm, yyyy-MM-dd HH:mm:ss, or ISO-8601 formats");
                    }
                }
            }
        }


        protected SimpleDateFormat getDateFormat(String pattern) {
            SimpleDateFormat result= new SimpleDateFormat(pattern);
            result.setLenient(true);
            return result;
        }
    }


}
