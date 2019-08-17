package com.cn.base.util;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateUtils {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String YEAR_PATTERN = "yyyy";
    public static final String MONTH_ONLY_PATTERN = "MM";
    public static final String TIME_PATTERN = "HH:mm:ss";

    /**
     * 计算两个日期的日期差
     *
     * @param dateOne
     * @param dateTwo
     * @return
     */
    public static long getBetweenDays(Date dateOne, Date dateTwo) {
        long from = dayBegin(dateOne).getTime();
        long to = dayBegin(dateTwo).getTime();

        if (to > from) {
            long days = (to - from) / (24 * 3600 * 1000);

            return days;
        } else {
            return 0L;
        }
    }

    /**
     * 计算两个时间的小时差
     *
     * @return ${return_type}
     * @throws
     * @author WangHan
     * @date 2018/8/22 20:16
     */
    public static long getBetweenHours(Date dateOne, Date dateTwo) {
        long from = dateOne.getTime();
        long to = dateTwo.getTime();
        return (to - from) / (1000 * 60 * 60);
    }

    /**
     * 返回特定日期的零点时间 </br> 2018-01-18 00:00:00
     *
     * @param date
     * @return
     */
    public static Date dayBegin(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c.getTime();
    }

    /**
     * 本月第一天
     *
     * @return
     * @throws ParseException
     */
    public static Date getMonthFirstDay() throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }

    /**
     * N个月前第一天
     *
     * @return
     * @throws ParseException
     */
    public static Date getMonthFirstDay(Integer offset) {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, offset);
        ca.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当月第一天
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            System.out.println(getLastDayOfMonth(-1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    public static Date getLastDayOfMonth(int amount) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, amount);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }



    /**
     * 传入时间的月第一天
     *
     * @return
     * @throws ParseException
     */
    public static Date getMonthFirstDay(Date date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }

    /**
     * 本月最后一天
     *
     * @return
     * @throws ParseException
     */
    public static Date getMonthLastDay() throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }


    /**
     * 本月最后一天
     *
     * @return
     * @throws ParseException
     */
    public static Date getMonthLastDay(Date date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }

    /**
     * 日期相加减天数
     *
     * @param date        如果为Null，则为当前时间
     * @param days        加减天数
     * @param includeTime 是否包括时分秒,true表示包含
     * @return
     * @throws ParseException
     */
    public static Date dateAdd(Date date, int days, boolean includeTime) throws ParseException {
        if (date == null) {
            date = new Date();
        }
        if (!includeTime) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN);
            date = sdf.parse(sdf.format(date));
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * 时间格式化成字符串
     *
     * @param date    Date
     * @param pattern StringUtils.DATE_TIME_PATTERN || StringUtils.DATE_PATTERN， 如果为空，则为yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static String dateFormat(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString String
     * @param pattern        StringUtils.DATE_TIME_PATTERN || StringUtils.DATE_PATTERN，如果为空，则为yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static Date dateParse(String dateTimeString, String pattern) throws ParseException {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateTimeString);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString String
     * @return
     * @throws ParseException
     */
    public static Date dateParseByLength(String dateTimeString) throws ParseException {
        int length = dateTimeString.length();
        String pattern = DateUtils.DATE_PATTERN;
        if (length > 11) {
            pattern = DateUtils.DATE_TIME_PATTERN;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateTimeString);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return DateUtils.dayBegin(sdf.parse(dateTimeString));
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString String
     * @param pattern        StringUtils.DATE_TIME_PATTERN || StringUtils.DATE_PATTERN，如果为空，则为yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static Date dateParse(String dateTimeString, String pattern, TimeZone zone) throws ParseException {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(zone);
        return sdf.parse(dateTimeString);
    }


    /**
     * 将日期时间格式成只有日期的字符串（可以直接使用dateFormat，Pattern为Null进行格式化）
     *
     * @param dateTime Date
     * @return
     */
    public static String dateTimeToDateString(Date dateTime) {
        String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);
        return dateTimeString.substring(0, 10);
    }

    /**
     * 当时、分、秒为00:00:00时，将日期时间格式成只有日期的字符串，
     * 当时、分、秒不为00:00:00时，直接返回
     *
     * @param dateTime Date
     * @return
     */
    public static String dateTimeToDateStringIfTimeEndZero(Date dateTime) {
        String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);
        if (dateTimeString.endsWith("00:00:00")) {
            return dateTimeString.substring(0, 10);
        } else {
            return dateTimeString;
        }
    }

    /**
     * 将日期时间格式成日期对象，和dateParse互用
     *
     * @param dateTime Date
     * @return Date
     */
    public static Date dateTimeToDate(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 时间加减小时
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param hours     加减的小时
     * @return Date
     */
    public static Date dateAddHours(Date startDate, int hours) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + hours);
        return c.getTime();
    }

    /**
     * 时间加减分钟
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param minutes   加减的分钟
     * @return
     */
    public static Date dateAddMinutes(Date startDate, int minutes) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);
        return c.getTime();
    }

    /**
     * 时间加减秒数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param seconds   加减的秒数
     * @return
     */
    public static Date dateAddSeconds(Date startDate, int seconds) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + seconds);
        return c.getTime();
    }

    /**
     * 时间加减天数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param days      加减的天数
     * @return Date
     */
    public static Date dateAddDays(Date startDate, int days) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + days);
        return c.getTime();
    }

    /**
     * 时间加减月数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param months    加减的月数
     * @return Date
     */
    public static Date dateAddMonths(Date startDate, int months, TimeZone zone) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance(zone);
        c.setTime(startDate);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
        return c.getTime();
    }

    /**
     * 时间加减周
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param weeks     加减的周数
     * @return Date
     */
    public static Date dateAddWeeks(Date startDate, int weeks) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) + weeks);
        return c.getTime();
    }

    /**
     * 时间加减年数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param years     加减的年数
     * @return Date
     */
    public static Date dateAddYears(Date startDate, int years) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + years);
        return c.getTime();
    }

    /**
     * 时间比较（如果myDate>compareDate返回1，<返回-1，相等返回0）
     *
     * @param myDate      时间
     * @param compareDate 要比较的时间
     * @return int
     */
    public static int dateCompare(Date myDate, Date compareDate) {
        Calendar myCal = Calendar.getInstance();
        Calendar compareCal = Calendar.getInstance();
        if (myDate == null && compareDate == null) {
            return 0;
        } else if (myDate == null) {
            return -1;
        } else if (compareDate == null) {
            return 1;
        }
        myCal.setTime(myDate);
        compareCal.setTime(compareDate);
        return myCal.compareTo(compareCal);
    }

    /**
     * 获取两个时间中最小的一个时间
     *
     * @param date
     * @param compareDate
     * @return
     */
    public static Date dateMin(Date date, Date compareDate) {
        if (date == null) {
            return compareDate;
        }
        if (compareDate == null) {
            return date;
        }
        if (1 == dateCompare(date, compareDate)) {
            return compareDate;
        } else if (-1 == dateCompare(date, compareDate)) {
            return date;
        }
        return date;
    }

    /**
     * 获取两个时间中最大的一个时间
     *
     * @param date
     * @param compareDate
     * @return
     */
    public static Date dateMax(Date date, Date compareDate) {
        if (date == null) {
            return compareDate;
        }
        if (compareDate == null) {
            return date;
        }
        if (1 == dateCompare(date, compareDate)) {
            return date;
        } else if (-1 == dateCompare(date, compareDate)) {
            return compareDate;
        }
        return date;
    }

    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int dateBetweenYear(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24 / 365);
    }

    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int dateBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24);
    }

    public static long dateBetweenSeconds(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / 1000;
    }

    public static long dateBetweenHours(Date startDate, Date endDate) {
        return dateBetweenSeconds(startDate, endDate) / 60;
    }


    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     *
     * @param date
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static boolean isDateBetween(Date date, Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return true;
        return date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime();

    }

    /**
     * 获取两个日期（不含时分秒）相差的天数，包含今天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int dateBetweenIncludeToday(Date startDate, Date endDate) {
        return dateBetween(startDate, endDate) + 1;
    }

    /**
     * 获取日期时间的年份，如2017-02-13，返回2017
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取日期时间的月份，如2017年2月13日，返回2
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期时间的第几天（即返回日期的dd），如2017-02-13，返回13
     *
     * @param date
     * @return
     */
    public static int getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取日期时间当月的总天数，如2017-02-13，返回28
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取日期时间当年的第几天，如2017-02-13，返回2017年的53
     *
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取日期时间当年的总天数，如2017-02-13，返回2017年的总天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }


    /**
     * 根据时间获取当月最大的日期
     * <li>2017-02-13，返回2017-02-28</li>
     * <li>2016-02-13，返回2016-02-29</li>
     * <li>2016-01-11，返回2016-01-31</li>
     *
     * @param date Date
     * @return
     * @throws Exception
     */
    public static Date maxDateOfMonth(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMaximum(Calendar.DATE);
        return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }

    /**
     * 根据时间获取当月最小的日期，也就是返回当月的1号日期对象
     *
     * @param date Date
     * @return
     * @throws Exception
     */
    public static Date minDateOfMonth(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMinimum(Calendar.DATE);
        return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }

    /**
     * 根据时间获取当月最小的日期，也就是返回当月的1号日期对象
     *
     * @param time 整形时间
     * @return
     */
    public static Date parse(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.getTime();
    }

    public static Date parseShort(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time * 1000);
        return cal.getTime();
    }


    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static Date getDateEndOfTheDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getDateStartOfTheDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Long computeSecondDiff(Date startTime, Date endTime) {
        return (endTime.getTime() - startTime.getTime()) / 1000L;
    }

    /**
     * 返回当前日期
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 返回明日日期
     *
     * @return
     */
    public static Date tomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return calendar.getTime();
    }

    /**
     * 格式化时间 HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return new SimpleDateFormat(TIME_PATTERN).format(date);
    }

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        if (date == null)
            return null;
        return new SimpleDateFormat(DATE_TIME_PATTERN).format(date);
    }

    /**
     * 格式化日期 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    /**
     * 返回特定日期偏移后的日期
     *
     * @param field
     * @param amount
     * @return
     */
    public static Date dateOffset(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, amount);
        return c.getTime();
    }


//    public static void main(String[] args) throws Exception {
//        System.out.println(checkBirthday(new Date(), DateUtils.dateParse("1995-02-13", DATE_PATTERN)));
//
//    }


    public static Date addHourMinutesSecond(Date date) {
        Calendar c = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        return c.getTime();
    }

    /**
     * 获取10天的时长
     *
     * @return
     */
    public static long tenDaysTime() {
        return 24 * 3600 * 1000 * 10;
    }

    public static Date setHotTime(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 14);
        return c.getTime();
    }

    /**
     * 是否是晚间，6-18点为晚间
     *
     * @param now
     * @return
     */
    public static boolean isEvening(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 6);
        Date beginTime = c.getTime();

        c.setTime(now);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 18);
        Date endTime = c.getTime();
        return !isEffectiveDate(now, beginTime, endTime);
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，不包含临界时间
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }

    /**
     * 检查当前日期是否生日
     *
     * @param currentDate
     * @param birthday
     * @return
     */
    public static Boolean checkBirthday(Date currentDate, Date birthday) {
        if (birthday != null && currentDate != null) {
            return getMonth(currentDate) == getMonth(birthday) &&
                    getDate(currentDate) == getDate(birthday);
        } else {
            return false;
        }
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static Date timeStampToDate(Long seconds) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(new Date(Long.valueOf(seconds + "000")));
        return calendar.getTime();
    }


    public static Integer getCurrentMonth(Date date) {
        if (date == null)
            return null;
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.MONTH) + 1;
    }

    public static Date getFirstOfThisWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int d = 0;
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            d = -6;
        } else {
            d = 2 - cal.get(Calendar.DAY_OF_WEEK);
        }
        cal.add(Calendar.DAY_OF_WEEK, d);
        return cal.getTime();
    }

    public static Date getBeginWeekdayOfLastWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return cal.getTime();
    }

    public static Date getEndWeekdayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginWeekdayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 5);
        return cal.getTime();
    }

    public static Date getBeginWeekendOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginWeekdayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 5);
        return cal.getTime();
    }

    public static Date getEndWeekendOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginWeekdayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    public static String getDateString(Date date, int extraDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_WEEK, extraDays);
        Date newDate = cal.getTime();
        return formatDate(newDate);
    }

    /**
     * 输入日期获取当前季度的开始时间
     *
     * @param date
     * @return
     */
    public static Date getQuarter(Date date) {
        SimpleDateFormat longSdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        SimpleDateFormat shortSdf = new SimpleDateFormat(DATE_PATTERN);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 0);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 3);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 6);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 9);
            }
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;

    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }


}