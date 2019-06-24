package com.cjp.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:日期相关工具类
 * @Author: sijie.chen sijie.chen@luckincoffee.com
 * @Date: 2019-03-15
 */
public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH = "yyyy-MM-dd-HH";
    public static final String YYYYMMDDHH = "yyyyMMddHH";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String HH_MM_SS = "HH:mm:ss";

    /**
     * @Description: 获取当前格式化时间的字符串
     * @Param: []
     * @return: java.lang.String
     */
    public static String getCurDate() {
        String curDate = DateFormatUtils.format(new Date(), YYYY_MM_DD_HH_MM_SS);
        return curDate;
    }

    /**
     * @Description: 获取日期对应在一周里是第几天
     * @Param: [dateString]
     * @return: int
     */
    public static int getDayOfWeek(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        try {
            Date date = format.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            int tmp = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (0 == tmp) {
                tmp = 7;
            }
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @Description: 将Date日期按指定格式转为字符串
     * @Param: [date, pattern]
     * @return: java.lang.String
     */
    public static String dateToString(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(date);
        }
        return null;
    }

    /**
     * @Description: 将字符串转为日期
     * @Param: [str, pattern]
     * @return: java.util.Date
     */
    public static Date stringToDate(String str, String pattern) throws ParseException {
        if (str != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(str);
        }
        return null;
    }

    /**
     * 获取几天前或者几天后的时间
     *
     * @param format
     * @param date
     * @param dayNum
     * @return
     */
    public static String getBeforeOrAfterDate(String format, String date, int dayNum) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date dt = stringToDate(date, format);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            calendar.add(Calendar.DATE, dayNum);
            String strTime = sdf.format(calendar.getTime());
            return strTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

}
