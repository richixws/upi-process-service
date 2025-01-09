package pe.gob.bcrp.upi.process.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class ParseUtility {

    private ParseUtility() {
        super();
    }

    public static Optional<Date> parseDateValue(String dateString) {
        try {
            DateFormat format = getDateFormat("dd/MM/yyyy HH:mm");
            return Optional.ofNullable(format.parse(dateString));
        } catch (Exception ex) {
            try {
                DateFormat ob = getDateFormat("yyyy-mm-dd HH:mm:ss");
                return Optional.ofNullable(ob.parse(dateString));
            } catch (Exception e) {
                try {
                    DateFormat ob = getDateFormat("yyyy-mm-dd HH:mm");
                    return Optional.ofNullable(ob.parse(dateString));
                } catch (Exception exc) {
                    return Optional.empty();
                }
            }
        }
    }


    private static DateFormat getDateFormat(String pattern) {
        SimpleDateFormat result;
        result = new SimpleDateFormat(pattern);
        result.setLenient(true);
        return result;
    }
}
