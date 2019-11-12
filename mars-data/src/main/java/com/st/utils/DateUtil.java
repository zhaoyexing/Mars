package com.st.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期辅助类
 * Created by zhaoyx on 2016/9/1.
 */
public class DateUtil {

    private static final String date_format = "yyyy-MM-dd";
//    private static  final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // 日期格式
    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<>();

    public static DateFormat getDateFormat()
    {
        DateFormat df = threadLocal.get();
        if(df==null){
            df = new SimpleDateFormat(date_format);
            threadLocal.set(df);
        }
        return df;
    }

    public static <T> String formatDate(T t) {
        return getDateFormat().format(t);
    }

    public static Date parse(String strDate) throws ParseException {
        return getDateFormat().parse(strDate);
    }

    /**
     * 时间戮转成string
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

    public static Date addDateFor(Date date,long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

   /**
     * 获取当前日期前dayNum天
     * @param date
    *  dayNum 为正数，后N天，为正数前N天
     * @return
     */
    public static Date getNextDay(Date date,int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, dayNum);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获取当前日期下一个月
     * @param date
     * @return
     */
    public static Date getNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        date = calendar.getTime();
        return date;
    }
    /**
     * util日期转成字符串
     * @param date
     * @param date_format
     * @return
     */
    public static  String dateConvertToStr(Date date,String date_format){

        //接收字符串参数date_format如2016-09-01
        SimpleDateFormat sdf =   new SimpleDateFormat( date_format );
        //格式化日期
        String str = sdf.format(date);
        return  str;
    }

    /**
     * 字符串转化为日期
     * @param dateStr
     * @param datePattern
     * @return
     */
    public static Date strConvertToDate(String dateStr,String datePattern){
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    /*
     * 返回当前时间相隔多长时间的戮字符串
     * @param hour
     * @return
     */
    public static String getUnixTimestamp(int hour){
        return  String.valueOf((new Date().getTime() - 1000 * 60 * 60 *hour) / 1000);
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     * @param beginDate
     * @param endDate
     * @return List<Date>
     */
    @SuppressWarnings("unchecked")
    public static  Map<String,Object> getDatesBetweenTwoDate(Date beginDate, Date endDate,Object value) {
        Map<String,Object> dateMap = new TreeMap<>();
        Calendar cal = Calendar.getInstance();
        dateMap.put(String.valueOf(beginDate.getTime()/1000),value);
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                dateMap.put(String.valueOf(cal.getTime().getTime()/1000),value);
            } else {
                break;
            }
        }
        dateMap.put(String.valueOf(endDate.getTime()/1000),value);
        return dateMap;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     * @param beginDate
     * @param endDate
     * @return List<Date>
     */
    @SuppressWarnings("unchecked")
    public static  Map<String,Object> getMonthDatesBetweenTwoDate(Date beginDate, Date endDate,Object value) {
        Map<String,Object> dateMap = new TreeMap<>();
        Calendar cal = Calendar.getInstance();
        dateMap.put(String.valueOf(beginDate.getTime()/1000),value);
        //使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                dateMap.put(String.valueOf(cal.getTime().getTime()/1000),value);
            } else {
                break;
            }
        }
        dateMap.put(String.valueOf(endDate.getTime()/1000),value);
        return dateMap;
    }

    /**
     *
     * @param beginDate
     * @param endDate
     * @param value  默认处处的值
     * @param dataFormat
     * @return
     */

    @SuppressWarnings("unchecked")
    public static  Map<String,Object> getDatesBetweenTwoDate(Date beginDate, Date endDate,Object value,String dataFormat) {
        Map<String,Object> dateMap = new TreeMap<>();
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        dateMap.put(dateConvertToStr(beginDate,dataFormat),value);
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                dateMap.put(dateConvertToStr(cal.getTime(),dataFormat),value);
            } else {
                break;
            }
        }
        dateMap.put(dateConvertToStr(endDate,dataFormat),value);
        return dateMap;
    }


    @SuppressWarnings("unchecked")
    public static  Map<String,Object> getMonthDatesBetweenTwoDate(Date beginDate, Date endDate,Object value,String dataFormat) {
        Map<String,Object> dateMap = new TreeMap<>();
        Calendar cal = Calendar.getInstance();
        //使用给定的 Date 设置此 Calendar 的时间
        dateMap.put(dateConvertToStr(beginDate,dataFormat),value);
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            //根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                dateMap.put(dateConvertToStr(cal.getTime(),dataFormat),value);
            } else {
                break;
            }
        }
        dateMap.put(dateConvertToStr(endDate,dataFormat),value);
        return dateMap;
    }





}
