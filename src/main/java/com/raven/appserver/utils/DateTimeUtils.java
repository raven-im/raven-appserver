package com.raven.appserver.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * @author: bbpatience
 * @date: 2018/10/20
 * @description: DateTimeUtils
 **/
public class DateTimeUtils {
    public static Date currentUTC() {
        return new DateTime(DateTimeZone.UTC).toDate();
    }

    public static Date getDate(String timeStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentUTC();
    }
}
