package com.tim.appserver.utils;

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
}
