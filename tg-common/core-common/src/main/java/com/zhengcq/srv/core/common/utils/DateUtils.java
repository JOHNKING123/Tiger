package com.zhengcq.srv.core.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm", "yyyy-MM",
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	public static final String DAYPATTERN = "yyyy-MM-dd";
	public static final String MONTHPATTERN = "yyyy-MM";
	public static final String DAYHMPATTERN = "yyyy-MM-dd HH:mm";
	public static final String DAYHMSPATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String FULL_DAYHMPATTERN = "yyyy-MM-dd HH:mm.SSS";

	public static final String YYMMPATTERN = "yyMM";
	public static final String YYMMddPATTERN = "yyMMdd";

	public static final String YYMMDDPATTERN =  "yyyyMMdd";
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 *
	 * @param hm  例如:  19:22
	 * @return
	 */
	public static Date getDateByHMStr(String hm){
		SimpleDateFormat sdf  =   new SimpleDateFormat( DAYHMPATTERN );
		try {
			return sdf.parse(DateUtils.getDate(DAYPATTERN)+" "+hm);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		try{
			if (pattern != null && pattern.length > 0) {
				formatDate = DateFormatUtils.format(date, pattern[0].toString());
			} else {
				formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return formatDate;
	}

	public static Date parseDate(String dateStr, String pattern) {
		if(dateStr == null || dateStr.length() == 0) {
			// fixed 2018-01-23
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 获取今天0时0分0秒
	 */
	public static Date getToday() {
		/*long current=System.currentTimeMillis();//当前时间毫秒数
		long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		return new Date(zero);*/

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date zero = calendar.getTime();
		return zero;
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateTime(Date date){
		return formatDate(date,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getHour(Date date) {
		return formatDate(date, "HH");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	public static String getYearMonth(){
		return formatDate(new Date(), "yyyy-MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	public static String getWeekNumber(Date date) {
		String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if(week_index<0){
			week_index = 0;
		}
		return weeks[week_index];

	}	/**
	*返回当前属于当年中的第几个星期
	*/
	public static int getWeekOfYear(Date date) {
		if(date == null){
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }

	public static Date parseDateFromTimestamp(long timeMillis){
		try {
			Timestamp ts = new Timestamp(timeMillis);
			//Date date = new Date();
			//date = ts;
			//return date;
			return (Date)ts;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDateTime(long timeMillis){
		Date date = parseDate(timeMillis);
		if( date != null){
			return getDateTime(date);
		}
		return null;
	}
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/*
	 * 获取前x天，xday则为负数
	 * 获取后x天，xday则为正数
	 */
	public static String getBeforeDate(String pattern,int xDay){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,xDay);
		return DateFormatUtils.format(cal.getTime(), pattern);
	}

	public static Date getBeforeDate(Date date, int xDay) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH) + xDay);
		return calendar.getTime();
	}

	public static Date getBeforeDay(int xDay) {
		return DateUtils.parseDate(getBeforeDate(xDay));
	}

	public static String getBeforeDate(int xDay){
		return getBeforeDate("yyyy-MM-dd",xDay);
	}


	public static Date getFirstDateOfMonth() {
		Calendar cal = Calendar.getInstance();//获取当前日期
		cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);

		return cal.getTime();
	}

	public static Date getFirstDateOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);//获取当前日期
		cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);

		return cal.getTime();
	}

	public static Date getFirstDateOfLastMonth(Date date) {
		return DateUtils.addMonths(getFirstDateOfMonth(date), -1);
	}

	public static Date getThisWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		return cal.getTime();
	}

	public static Date getLastWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMonday(date));
		cal.add(Calendar.DATE, -7);
		return cal.getTime();
	}

	public static Date getNextWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMonday(date));
		cal.add(Calendar.DATE, 7);
		return cal.getTime();
	}

	//判断选择的日期是否是今天
	public static boolean isToday(long time)
	{
		return isThisTime(time,"yyyy-MM-dd");
	}
	//判断选择的日期是否是本月
	public static boolean isThisMonth(long time)
	{
		return isThisTime(time,"yyyy-MM");
	}
	private static boolean isThisTime(long time,String pattern) {
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String param = sdf.format(date);//参数时间
		String now = sdf.format(new Date());//当前时间
		if(param.equals(now)){
			return true;
		}
		return false;
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse("2018-03-01");
			System.out.println("今天是" + sdf.format(date));
			System.out.println("上周一" + sdf.format(getFirstDateOfLastMonth(date)));
			System.out.println("本周一" + sdf.format(getFirstDateOfMonth(date)));
			System.out.println("下周一" + sdf.format(getNextWeekMonday(date)));

			System.out.println(getBeforeDate(date, -1));
			System.out.println(getBeforeDay(-1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取系统前时间.
	 *
	 * @param minute the minute
	 *
	 * @return the before time[yyyy-MM-dd HH:mm:ss]
	 */
	public static String getBeforeTime(int minute) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());

		calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE) - minute);
		return DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd HH:mm:s");

	}
}
