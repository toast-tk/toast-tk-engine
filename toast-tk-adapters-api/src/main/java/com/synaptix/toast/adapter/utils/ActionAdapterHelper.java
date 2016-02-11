package com.synaptix.toast.adapter.utils;

import java.math.BigDecimal;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatterBuilder;

import com.google.common.base.CaseFormat;
import com.synaptix.toast.adapter.swing.AbstractSwingPage;
import com.synaptix.toast.adapter.swing.ISwingComponentFactory;

public class ActionAdapterHelper {
	
	private static final Logger LOG = LogManager.getLogger(ActionAdapterHelper.class);

	/**
	 * Format pattern for LocalTime
	 */
	public static final String TIME_PATTERN = "HH:mm:ss";

	/**
	 * Format pattern for LocalDate
	 */
	public static final String DATE_PATTERN = "dd/MM/yyyy";

	/**
	 * Format pattern for LocalDateTime
	 */
	public static final String DATETIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

	public static boolean isEmptyOrNull(final String str) {
		return StringUtils.isEmpty(str) || str.equals("null") || str.equals("ï¿½") || str.equals("�");
	}

	public static boolean isNotEmptyOrNull(final String str) {
		return !isEmptyOrNull(str);
	}
	
	/**
	 * Returns a LocalDateTime from a string in format "dd/MM/yyyy HH:mm:ss"
	 */
	public static LocalDateTime parseLocalDateTimeFromString(final String dateTime) {
		return isNotEmptyOrNull(dateTime) ? LocalDateTime.parse(dateTime, new DateTimeFormatterBuilder().appendPattern(DATETIME_PATTERN).toFormatter()) : null;
	}

	/**
	 * Returns a LocalDate from a string in format "dd/MM/yyyy"
	 */
	public static LocalDate parseDateFromString(final String date) {
		return isNotEmptyOrNull(date) ? LocalDate.parse(date, new DateTimeFormatterBuilder().appendPattern(DATE_PATTERN).toFormatter()) : null;
	}

	/**
	 * Returns a LocalTime from a string in format "HH:mm:ss"
	 */
	public static LocalTime parseTimeFromString(final String time) {
		if(isEmptyOrNull(time)) {
			return null;
		}
		return isNotEmptyOrNull(time) ? LocalTime.parse(time, new DateTimeFormatterBuilder().appendPattern(TIME_PATTERN).toFormatter()) : null;
	}

	public static Double parseDouble(final String str) {
		return isNotEmptyOrNull(str) ? Double.valueOf(Double.parseDouble(str)) : null;
	}

	/**
	 * Returns the string, or null if the string is null or empty or equals to "null".
	 * 
	 * @param str
	 * @return
	 */
	public static String getString(final String str) {
		return isEmptyOrNull(str) ? null : str;
	}

	public static String stringToHexString(String str) {
		Pattern p = Pattern.compile("ID\\(([0-9A-F]+)\\)");
		Matcher m = p.matcher(str);
		if(m.matches()) {
			return m.group(1);
		}
		if(str.length() <= 16) {
			str = StringUtils.rightPad(str, 16);
		}
		else {
			str.substring(0, 16);
		}
		final StringBuilder builder = new StringBuilder(str.length() * 2);
		for(int i = 0; i < str.length(); i++) {
			builder.append(Integer.toHexString(str.charAt(i)).toUpperCase());
		}
		return builder.toString();
	}

	/**
	 * Parse boolean
	 * 
	 * @param str
	 *            , if null then false
	 * @return
	 */
	public static boolean parseBoolean(final String str) {
		return isNotEmptyOrNull(str) && Boolean.parseBoolean(str);
	}

	/**
	 * Parse enum
	 * 
	 * @param str
	 * @param enumType
	 * @return
	 */
	@SuppressWarnings({
		"unchecked", 
		"rawtypes"
	})
	public static Enum<?> parseEnum(
		final Class<? extends Enum> enumType,
		String str
	) {
		if(isNotEmptyOrNull(str)) {
			if(isNumeric(str)) {
				str = '_' + str;
			}
			return Enum.valueOf(enumType, str);
		}
		return null;
	}

	public static boolean isNumeric(final String str) {
		return str.matches("\\d+"); // match a number with optional '-' and
// decimal.
	}

	public static BigDecimal parseBigDecimal(final String str) {
		return isNotEmptyOrNull(str) ? new BigDecimal(str) : null;
	}

	public static Class<?> loadClass(final String nameClass) {
		try {
			return ActionAdapterHelper.class.getClassLoader().loadClass(nameClass);
		}
		catch(final ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
			return null;
		}
	}

	public static Integer parseInteger(final String str) {
		return isNotEmptyOrNull(str) ? new Integer(str) : null;
	}

	public static Duration parseDuration(final String str) {
		return new Duration(parseLong(str));
	}

	public static Duration parseDurationFromTime(String time) {
		return StringUtils.isNotEmpty(time) ? Duration.millis(LocalTime.parse(
			time,
			new DateTimeFormatterBuilder().appendPattern(TIME_PATTERN).toFormatter()).getMillisOfDay()) : null;
	}

	/**
	 * @param propertyValue
	 * @return
	 */
	public static Long parseLong(final String str) {
		return isNotEmptyOrNull(str) ? Long.valueOf(Long.parseLong(str)) : null;
	}

	public static String parseTestString(String text) {
		if(text != null) {
			text = text.trim().replaceAll(" +", " ");
			if(text.contains(" ")) {
				text = text.toLowerCase().replace(" ", "_");
				text = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, text);
			}
			if(text.contains("_")) {
				text = text.toLowerCase();
				text = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, text);
			}
			else {
				text = StringUtils.uncapitalize(text);
			}
			return text;
		}
		return null;
	}

	/**
	 * Convert a RawId (example: "44445032202020202020202020202020 ") to a String (example: DDP2).
	 * 
	 * @param str
	 *            Hexadecimal string
	 * @return String
	 */
	public static String decodeId(final String str) {
		return isEmptyOrNull(str) ? null : hexStringToString(str);
	}

	private static String hexStringToString(final String hex) {
		if(hex.length() % 2 != 0) {
			LOG.error("requires EVEN number of chars");
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		// Convert Hex 0232343536AB into two characters stream.
		for(int i = 0; i < hex.length() - 1; i += 2) {
			// Grab the hex in pairs
			final String output = hex.substring(i, (i + 2));
			// Convert Hex to Decimal
			final int decimal = Integer.parseInt(output, 16);
			sb.append((char) decimal);
		}
		return sb.toString().trim();
	}
}