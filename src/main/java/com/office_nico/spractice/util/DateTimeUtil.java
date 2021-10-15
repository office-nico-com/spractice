package com.office_nico.spractice.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

	// 時間フォーマット
	public static class DateTimeFormat {

		// できるだけ長いフォーマットを先頭に
		public static String TIME_MIN_SLASH_ZERO_PADDING = "yyyy/MM/dd HH:mm";
		public static String TIME_MIN_SLASH_ZERO_SUPPRESS1 = "yyyy/M/d H:mm";
		public static String TIME_MIN_SLASH_ZERO_SUPPRESS2 = "yyyy/M/d H:m";

		public static String TIME_SEC_SLASH_ZERO_PADDING = "yyyy/MM/dd HH:mm:ss";
		public static String TIME_SEC_SLASH_ZERO_SUPPRESS1 = "yyyy/M/d H:mm:ss";
		public static String TIME_SEC_SLASH_ZERO_SUPPRESS2 = "yyyy/M/d H:m:s";

		public static String TIME_MIN_HYPHEN_ZERO_PADDING = "yyyy-MM-dd HH:mm";
		public static String TIME_MIN_HYPHEN_ZERO_SUPPRESS1 = "yyyy-M-d H:mm";
		public static String TIME_MIN_HYPHEN_ZERO_SUPPRESS2 = "yyyy-M-d H:m";

		public static String TIME_SEC_HYPHEN_ZERO_PADDING = "yyyy-MM-dd HH:mm:ss";
		public static String TIME_SEC_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssX";
		public static String TIME_SEC_HYPHEN_ZERO_SUPPRESS1 = "yyyy-M-d H:mm:ss";
		public static String TIME_SEC_HYPHEN_ZERO_SUPPRESS2 = "yyyy-M-d H:m:s";

		public static String TIME_MIN_JA_ZERO_PADDING = "yyyy年MM月dd日 HH時mm分";
		public static String TIME_MIN_JA_ZERO_SUPPRESS1 = "yyyy年M月d日 H時mm分";
		public static String TIME_MIN_JA_ZERO_SUPPRESS2 = "yyyy年M月d日 H時m分";

		public static String TIME_SEC_JA_ZERO_PADDING = "yyyy年MM月dd日 HH時mm分ss秒";
		public static String TIME_SEC_JA_ZERO_SUPPRESS1 = "yyyy年M月d日 H時mm分ss秒";
		public static String TIME_SEC_JA_ZERO_SUPPRESS2 = "yyyy年M月d日 H時m分s秒";

		public static String DATE_SLASH_ZERO_PADDING = "yyyy/MM/dd";
		public static String DATE_SLASH_ZERO_SUPPRESS = "yyyy/M/d";
		public static String DATE_HYPHEN_ZERO_PADDING = "yyyy-MM-dd";
		public static String DATE_HYPHEN_ZERO_SUPPRESS = "yyyy-M-d";
		public static String DATE_JA_ZERO_PADDING = "yyyy年MM月dd日";
		public static String DATE_JA_ZERO_SUPPRESS = "yyyy年M月d日";

		public static String DATE_NUMBER = "yyyyMMdd";
		public static String TIME_MIN_NUMBER = "yyyyMMddHHmm";
		public static String TIME_SEC_NUMBER = "yyyyMMddHHmmss";

	}

	/**
	 * 現在日時を指定の日時型で結果を返す
	 * 
	 * @param type 結果の日時型（java.util.Date、LocalDateTime、LocalTime）
	 * @return 日時オブジェクト
	 */
	@SuppressWarnings("unchecked")
	public static <T> T now(Class<T> type) {
		Date ret = new Date();
		if (type.equals(Date.class)) {
			return (T) ret;
		}
		else if (type.equals(LocalDateTime.class)) {
			Instant instant = ret.toInstant();
			return (T) LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		}
		else if (type.equals(LocalDate.class)) {
			Instant instant = ret.toInstant();
			return (T) LocalDate.ofInstant(instant, ZoneId.systemDefault());
		}
		return null;

	}

	/**
	 * 現在日を指定の日時型で結果を返す
	 * 
	 * @param type 結果の日時型（java.util.Date、LocalDateTime、LocalTime）
	 * @return 日時オブジェクト
	 */
	@SuppressWarnings("unchecked")
	public static <T> T today(Class<T> type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date ret = cal.getTime();
		if (type.equals(Date.class)) {
			return (T) ret;
		}
		else if (type.equals(LocalDateTime.class)) {
			Instant instant = ret.toInstant();
			return (T) LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		}
		else if (type.equals(LocalDate.class)) {
			Instant instant = ret.toInstant();
			return (T) LocalDate.ofInstant(instant, ZoneId.systemDefault());
		}
		return null;

	}

	/**
	 * 文字列をパースして指定の日時型で結果を返す
	 * 
	 * @param dateString 日付文字列
	 * @param type       結果の日時型（java.util.Date、LocalDateTime、LocalTime）
	 * @return 日時オブジェクト
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parse(String dateString, Class<T> type) {

		Date ret = _parse(dateString);
		if (type.equals(Date.class)) {
			return (T) ret;
		}
		else if (type.equals(LocalDateTime.class)) {
			Instant instant = ret.toInstant();
			return (T) LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		}
		else if (type.equals(LocalDate.class)) {
			Instant instant = ret.toInstant();
			return (T) LocalDate.ofInstant(instant, ZoneId.systemDefault());
		}
		return null;
	}

	/**
	 * 時間情報を文字列にして返す
	 * 
	 * @param obj 日付オブジェクト
	 * @return 日時文字列
	 */
	public static String format(Date obj, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(obj);
	}

	/**
	 * 時間情報を文字列にして返す
	 * 
	 * @param obj 日付オブジェクト
	 * @return 日時文字列
	 */
	public static String format(LocalDateTime obj, String format) {

		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = ZonedDateTime.of(obj, zone);
		Instant instant = zonedDateTime.toInstant();
		Date date = Date.from(instant);
		return format(date, format);
	}

	/**
	 * 時間情報を文字列にして返す
	 * 
	 * @param obj 日付オブジェクト
	 * @return 日時文字列
	 */
	public static String format(LocalDate obj, String format) {
		SimpleDateFormat _format = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = _format.parse(obj.toString());
			return format(date, format);
		}
		catch (ParseException e) {
		}
		return null;
	}

	private static Date _parse(String dateString) {
		Date ret = null;
		Field[] fields = DateTimeFormat.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().equals(String.class)) {
				try {
					String format = (String) field.get(null);
					SimpleDateFormat sdf = new SimpleDateFormat(format);
					ret = sdf.parse(dateString);
					if (ret != null) {
						break;
					}
				}
				catch (IllegalArgumentException | IllegalAccessException | ParseException e) {
					// skip
				}
			}
		}
		return ret;
	}
	
	
	/**
	 * Date → LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		if(date == null) {
			return null;
		}
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * Date → LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime toLocalDateTime(java.sql.Date date) {
		if(date == null) {
			return null;
		}
		Instant instant = date.toInstant();
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * Timestamp → LocalDateTime
	 * @param date
	 * @return
	 */
	public static LocalDateTime toLocalDateTime(java.sql.Timestamp tm) {
		if(tm == null) {
			return null;
		}
		Instant instant = tm.toInstant();
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	/**
	 * LoclDateTime → Date
	 * @param localDateTime
	 * @return
	 */
	public static Date toDate(LocalDateTime localDateTime) {
		if(localDateTime == null) {
			return null;
		}
		ZoneId zone = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zone);
		Instant instant = zonedDateTime.toInstant();
		return Date.from(instant);
	}
}
