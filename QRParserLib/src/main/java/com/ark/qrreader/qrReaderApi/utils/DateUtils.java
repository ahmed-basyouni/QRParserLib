package com.ark.qrreader.qrReaderApi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by ahmedb on 5/31/16.
 */
public class DateUtils {

    private static final Map<String, String> DATE_FORMAT_REGEXPS_SCORE = new HashMap<String, String>() {{
        put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        put("^\\d{1,2}-[a-z]{3}-\\d{4}$", "dd-MMM-yyyy");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
    }};

    private static final Map<String, String> DATE_FORMAT_REGEXPS_SLASHES = new HashMap<String, String>() {{
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        put("^\\d{1,2}/[a-z]{3}/\\d{4}$", "dd/MMM/yyyy");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
    }};

    private static final Map<String, String> DATE_FORMAT_REGEXPS_SPACES = new HashMap<String, String>() {{
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");

    }};

    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        put("^\\d{8}$", "yyyyMMdd");
        put("^\\d{8}$", "yyyyMMdd");
        put("^\\d{12}$", "yyyyMMddHHmm");
        put("^\\d{14}$", "yyyyMMddHHmmss");
    }};


    public static String determineDateFormat(String dateString) {


        if (dateString.contains("/")) {
            return determineDateFormat(dateString, DATE_FORMAT_REGEXPS_SLASHES);
        } else {
            if (dateString.contains("-")) {
                return determineDateFormat(dateString, DATE_FORMAT_REGEXPS_SCORE);
            }
            if (dateString.contains(" ")) {
                return determineDateFormat(dateString, DATE_FORMAT_REGEXPS_SPACES);
            }
            return determineDateFormat(dateString, DATE_FORMAT_REGEXPS);
        }
    }

    public static String determineDateFormat(String dateString, Map<String, String> dateFormate) {

        for (String regexp : dateFormate.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return (String) dateFormate.get(regexp);
            }
        }
        return null;
    }

    public static Date getDate(int year, int month, int day, int hour, int min, int sec) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String getDateString(Date date) {

        return new SimpleDateFormat("dd-MM-yyyy").format(date);

    }

    public static Date getTimeGMT(String dateString){

        boolean isGMT = dateString.toLowerCase().contains("z");

        String formate = "";
        if(isGMT) {
            if(dateString.contains("-"))
                formate = "yyyy-MM-dd'T'hh:mm:ss'Z'";
            else if(dateString.contains("/"))
                formate = "yyyy/MM/dd'T'hh:mm:ss'Z'";
            else
                formate = "yyyyMMdd'T'hhmmss'Z'";

        }else {

            if(dateString.contains("-"))
                formate = "yyyy-MM-dd'T'hh:mm:ss";
            else if(dateString.contains("/"))
                formate = "yyyy/MM/dd'T'hh:mm:ss";
            else
                formate = "yyyyMMdd'T'hhmmss";

        }
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date startDate = new Date();

        try {
            startDate = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }


}
