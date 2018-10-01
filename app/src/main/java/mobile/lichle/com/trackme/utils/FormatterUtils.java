package mobile.lichle.com.trackme.utils;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mobile.lichle.com.trackme.Constants;

/**
 * Created by lichvl.dp on 9/28/2018.
 */

public class FormatterUtils {

    public static final int SECOND_PER_MINUTE = 60;
    public static final int SECOND_PER_HOUR = 3600;

    private static DecimalFormat sDecimalFormat;

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss");

    /**
     * @param time in seconds
     * @return hour(s):minute(s):second(s)
     */
    @SuppressLint("DefaultLocale")
    public static String convertTimeToString(final int time) {
        final int seconds = time % SECOND_PER_MINUTE;
        final int minutes = time / SECOND_PER_MINUTE;
        final int hours = time / SECOND_PER_HOUR;
//        String hourString, secondString, minuteString;
//        if(seconds < 10){
//            secondString =
//        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Convert distance to format: 0.00 km
     *
     * @return
     */
    public static String convertDistanceToString(float value) {
        DecimalFormat formatter = getDecimalFormat();
        return formatter.format(value) + " " + Constants.DISTANCE_UNIT;
    }

    /**
     * Convert velocity to format: 0.00 km/h
     *
     * @return
     */
    public static String converVelocityToString(float value) {
        DecimalFormat formatter = getDecimalFormat();
        return formatter.format(value) + " " + Constants.VELOCITY_UNIT;
    }

    public static DecimalFormat getDecimalFormat() {
        if (null == sDecimalFormat) {
            sDecimalFormat = new DecimalFormat();
            sDecimalFormat.setMaximumFractionDigits(2);
            sDecimalFormat.setMinimumFractionDigits(2);

        }
        return sDecimalFormat;
    }

    public static String convertTimeMilisecondToDate(String miliSeconds) {
        Date date = new Date(miliSeconds);
        return sDateFormat.format(date);
    }

}
