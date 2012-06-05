package com.morgan.design.seamlessbackup.domain;

import java.text.SimpleDateFormat;

public class Constants {

	public static final boolean LOG_OUT = true;
	public static final boolean RETRY = true;

	public static final String DEFAULT_DATE_TIME_FORMAT = "ddMMyyyyHHmmss-";
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);

}
