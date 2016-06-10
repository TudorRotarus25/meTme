package com.tudor.rotarus.unibuc.metme.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Tudor on 08.06.2016.
 */
public class Utilities {

    private String TAG = getClass().getSimpleName();

    private static Utilities instance;

    private Utilities() {

    }

    public static Utilities getInstance() {

        if (instance == null) {
            instance = new Utilities();
        }

        return instance;
    }

    public static String[] splitDateString(String stringDate) {

        String[] date = stringDate.split(" ");

        return date;
    }

    public static String getPrettyFullDateTime(String stringDate) {

        SimpleDateFormat fromResponse = new SimpleDateFormat("yyyy-MM-dd H:mm:ss", Locale.US);
        SimpleDateFormat toDate = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm", Locale.US);

        Calendar dateTime = Calendar.getInstance();
        try {
            dateTime.setTime(fromResponse.parse(stringDate));
            return toDate.format(dateTime.getTime());

        } catch (ParseException e) {
            return "";
        }
    }
}
