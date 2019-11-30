package com.itla.apppost.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public final class DateUtil {

	private DateUtil(){}

	public static String longToDateTimeFormartter(long dateTime) {

		LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), TimeZone
			.getDefault().toZoneId());

		return time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy' a las' hh:mm "));
	}

}
