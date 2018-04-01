package com.ef.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

	public static String LOG_FILE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static String COMMAND_LINE_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss";

	// Convert format 'yyyy-MM-dd HH:mm:ss.SSS' to 'yyyy-MM-dd.HH:mm:ss'
	public static LocalDateTime converDateFormat(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(COMMAND_LINE_DATE_FORMAT);
		String dateString = date.format(formatter);

		LocalDateTime logDate = LocalDateTime.parse(dateString, formatter);

		return logDate;
	}

	// Convert string date to LocalDateTime in 'yyyy-MM-dd.HH:mm:ss' format
	public static LocalDateTime convertStringDateToLocalDateTime(String date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(COMMAND_LINE_DATE_FORMAT);
		LocalDateTime logDate = LocalDateTime.parse(date, formatter);

		return logDate;
	}

	// Convert string date from log to LocalDateTime in 'yyyy-MM-dd HH:mm:ss.SSS' format
	public static LocalDateTime convertStringDateLogToLocalDateTime(String date) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOG_FILE_DATE_FORMAT);
		LocalDateTime logDate = LocalDateTime.parse(date, formatter);

		return logDate;
	}
}
