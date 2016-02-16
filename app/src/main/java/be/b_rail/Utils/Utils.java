package be.b_rail.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jawad on 16-02-16.
 */
public class Utils {

    public static String getFormatDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSSZ", Locale.FRENCH);
       /* Date d = new Date();
        try {
            d = sdf.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        Calendar calendar = null;
        try {
            calendar.setTime(new Date(sdf.parse(value).getTime()));
          //  Date dateTimeObj = ISODateTimeFormat.dateTime().parseDateTime(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.toString();
    }

    public static String getDurationString(long seconds) {

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    public static String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}
