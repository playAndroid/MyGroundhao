package blog.groundhao.com.mygroundhao.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by user on 2016/4/14.
 */
public class TimeUtils {

    public static String dateStringFormatGoodExperienceDate(String stringTime) {
        if (isNullString(stringTime)) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));

        String time;
        try {
            Date parse = simpleDateFormat.parse(stringTime);
            long distenceTime = new Date().getTime() - parse.getTime();
            if (distenceTime < 0L) {
                time = "0 minutes ago";
            } else {
                long n2 = distenceTime / 60000L; //60s
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
                if (n2 < 60L) {
                    time = String.valueOf(n2) + " minutes ago";
                } else if (n2 < 720L) {
                    time = String.valueOf(n2 / 60L) + " hours ago";
                } else {
                    time = dateFormat.format(parse);
                }
            }
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static boolean isNullString(String string) {
        return string == null || string.equals("") || string.equals("null");
    }
}
