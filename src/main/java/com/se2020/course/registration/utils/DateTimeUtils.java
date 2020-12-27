package com.se2020.course.registration.utils;


import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class DateTimeUtils {
    public static Instant parseDate(String date){
        Instant ret = null;
        date = date + "T00:00:00";
        try{
            ret = Instant.parse(date);
        } catch (IllegalArgumentException e){
            e.printStackTrace();

        }
        return ret;
    }

//    public static void main(String[] args){
//
//        DateTime dt = parseDate("2020-01-15").toDateTime();
//        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//        String dtStr = fmt.print(dt);
//        System.out.println(dtStr);
//    }
}
