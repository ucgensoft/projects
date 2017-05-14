package com.ucgen.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

	public static final int DAY_IN_MILISECONDS = 1000 * 60 * 60 * 24;
	
	public static final String READABLE_DATE_FORMAT = "MMM dd, yyyy";
	public static final String SHORT_DATE_FORMAT = "dd.MM.yyyy";
	public static final String SHORT_DATE_NO_SEPERATIR_FORMAT = "ddMMyyyy";
	public static final String LONG_DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
	public static final String LONG_DATE_FORMAT_H24 = "dd.MM.yyyy HH:mm:ss";
	public static final String LONG_DATE_YEAS_FIRST_FORMAT_H24 = "yyyy-MM-dd HH:mm:ss";
	public static final String DOT_SEPERATOR = ".";
	
	public static final String TIME_ZONE_ISTANBUL = "Europe/Istanbul";
	
	private static DateFormat createSimpleDateFormat(String format) {
		DateFormat dateFormat =  DateFormat.getDateInstance(DateFormat.FULL, new Locale("en-US"));
		//dateFormat.setTimeZone(TimeZone.getTimeZone("Turkey"));
		return dateFormat;
	}
	
	public static String format(Date date, String format) {
		if (date != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			//simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Turkey"));
			return simpleDateFormat.format(date);
		} else {
			return "";
		}
	}

	public static Date valueOf(String date, String format) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		//simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Turkey"));
		return simpleDateFormat.parse(date);
	}
	
	public static int getDayOfWeek(Date date) {
		return DateUtil.getCalendar(date).get(Calendar.DAY_OF_WEEK);
	}
	
	public static int getDayOfMonth(Date date) {
		return DateUtil.getCalendar(date).get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getMaxOfMonth(Date date) {
		Calendar cal = DateUtil.getCalendar(date);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static Calendar getCalendar(Date date) {
		//Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Turkey"), new Locale("tr", "TR"));
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		} else {
			cal.setTime(new Date());
		}
		return cal;
	}
	
	public static Date addTime(Date date, int timePeriod, int amount) {
		Calendar cal = DateUtil.getCalendar(date);
		cal.add(timePeriod, amount);
		return cal.getTime();
	}
	
	public static Date setTimePeriod(Date date, int timePeriod, int value) {
		Calendar cal = DateUtil.getCalendar(date);
		cal.set(timePeriod, value);
		return cal.getTime();
	}
	
	public static int getDatePart(Date date, int datePart) {
		if (date == null) {
			date = new Date();
		}
		int datePartValue = DateUtil.getCalendar(date).get(datePart);
		if (datePart == Calendar.MONTH) {
			return datePartValue + 1;
		} else {
			return datePartValue;
		}
	}
	
	public static Date truncate(Date date) throws ParseException {
		String strTruncatedDate = format(date, SHORT_DATE_FORMAT);
		return valueOf(strTruncatedDate, SHORT_DATE_FORMAT);
	}
	
	public static String accountPeriodToString(String accountingPeriod, String seperator) {
		return accountingPeriod.substring(4) + seperator + accountingPeriod.substring(0, 4);
	}
	
	public static boolean before(Date sourceDate, Date destDate, int dateFormat) {
		return DateUtil.compare(sourceDate, destDate, dateFormat) == CompareResult.LOWER.getValue();
	}

	public static boolean after(Date sourceDate, Date destDate, int dateFormat) {
		return DateUtil.compare(sourceDate, destDate, dateFormat) == CompareResult.BIGGER.getValue();
	}

	public static boolean equals(Date sourceDate, Date destDate, int dateFormat) {
		return DateUtil.compare(sourceDate, destDate, dateFormat) == CompareResult.EQUAL.getValue();
	}

	public static int compare(Date sourceDate, Date destDate, int dateFormat) {
		String strDateFormat = null;
		if (dateFormat == SimpleDateFormat.SHORT) {
			strDateFormat = "yyyyMMdd";
		} else {
			strDateFormat = "yyyyMMddHHmmss";
		}
		String strSource = format(sourceDate, strDateFormat);
		String strDest = format(destDate, strDateFormat);
		return Long.valueOf(strSource).compareTo(Long.valueOf(strDest));
	}

	public static long dateDiff(Date date1, Date date2, int timePeriod) {
		Long diffInMiliseconds = Math.abs(date1.getTime() - date2.getTime());
		return Math.abs(diffInMiliseconds / DAY_IN_MILISECONDS);
	}
	
}
