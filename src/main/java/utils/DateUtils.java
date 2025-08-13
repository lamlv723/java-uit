package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String pattern) {
        if (date == null)
            return null;
        return new SimpleDateFormat(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        if (dateStr == null)
            return null;
        return new SimpleDateFormat(pattern).parse(dateStr);
    }
}
