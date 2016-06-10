package be.b_rail.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Jawad on 16-02-16.
 */
public class Utils {

    public static String getTimeFromDate(String dateFromAPI) {
        Date date;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
        try {

            date = new Date((Long.valueOf(dateFromAPI)) * 1000);
            //Log.i("", "getMinutsFromDate: " + date.toString());
            return dateFormat.format(date);
        } catch (Exception e) {
            return dateFromAPI;
        }

    }
    public static long seconds(String dateFromAPI) {
        return Long.valueOf(dateFromAPI)/1000;
    }

    public static String getDurationString(long seconds) {

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        return twoDigitString(hours) + ":" + twoDigitString(minutes);
    }
    public static int getDurationTimeSeconds(long seconds_start, long second_end) {

        long s = seconds_start % 60;
        long m = ((seconds_start / 60) % 60)*60;
        long h = ((seconds_start / (60 * 60)) % 24)*3600;

        long s2 = second_end % 60;
        long m2 = ((second_end / 60) % 60)*60;
        long h2 = ((second_end / (60 * 60)) % 24)*3600;

        return (int)((s+m+h)-(s2+m2+h2));
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
