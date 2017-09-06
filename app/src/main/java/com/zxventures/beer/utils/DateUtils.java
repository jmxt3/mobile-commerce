package com.zxventures.beer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The Class DateUtils.
 */
public class DateUtils {

    /**
     * Gets the current date.
     *
     * @return the current date
     */
    public static String getCurrentDate() {
        // format the date
        final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return dateFormatter.format(new Date());
    }
}
