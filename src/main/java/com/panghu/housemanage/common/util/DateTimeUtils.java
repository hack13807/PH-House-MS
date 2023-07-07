package com.panghu.housemanage.common.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtils {

    /**
     * 指定格式，格式化Date
     *
     * @param date   日期
     * @param format 格式
     * @return {@link String}
     */
    public static String formatDate(Date date, String format){
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return formatLocalDate(localDate, format);
    }

    /**
     * 指定格式，格式化LocalDate
     *
     * @param localDate 当地日期
     * @param format    格式
     * @return {@link String}
     */
    public static String formatLocalDate(LocalDate localDate, String format){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDate.format(formatter);
    }
}
