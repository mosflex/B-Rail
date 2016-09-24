package be.ontime.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            return dateFormat.format(date);
        } catch (Exception e) {
            return dateFromAPI;
        }
    }

    public static String getDurationString(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return twoDigitString(hours) + ":" + twoDigitString(minutes);
    }
    public static int getDurationTimeSeconds(long seconds_start, long second_end) {
        return totalSeconds(seconds_start) - totalSeconds(second_end);
    }

    public static int totalSeconds(long seconds){
        long s = seconds % 60;
        long m = ((seconds / 60) % 60)*60;
        long h = ((seconds / (60 * 60)) % 24)*3600;
        return (int)(s+m+h);
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
