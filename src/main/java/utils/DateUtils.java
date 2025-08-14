package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Chuyển đổi một chuỗi ngày tháng sang đối tượng Date.
     * @param dateStr Chuỗi ngày tháng cần chuyển đổi, ví dụ "30/08/2025".
     * @return Đối tượng Date tương ứng.
     * @throws ParseException nếu chuỗi không hợp lệ.
     */
    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setLenient(false); // Không chấp nhận ngày tháng không hợp lệ (ví dụ: 30/02/2025)
        return formatter.parse(dateStr);
    }
}