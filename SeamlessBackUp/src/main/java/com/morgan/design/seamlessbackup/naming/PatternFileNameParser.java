package com.morgan.design.seamlessbackup.naming;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morgan.design.seamlessbackup.domain.Constants;

public class PatternFileNameParser implements FileNameParser {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static Pattern PARTS_PATTERN = Pattern.compile("\\[(.*?)\\]");

	//@formatter:off
	//Explanation:
	//	    \[ : [ is a meta char and needs to be escaped if you want to match it literally.
	//	    (.*?) : match everything in a non-greedy way and capture it.
	//	    \] : ] is a meta char and needs to be escaped if you want to match it literally.
	// 		It marks "Invalid escape sequence"
	//@formatter:on

	@Override
	public Date getDate(final String fileName) {
		try {
			final Matcher m = PARTS_PATTERN.matcher(fileName);
			if (m.find()) {
				final String datetime = m.group(1);
				log.debug("Matched pattern group, Datetime is {}", datetime);

				final Date date = Constants.DATE_TIME_FORMAT.parse(datetime);
				log.debug("Datetime succesfully converter to {}", date);

				return date;
			}
			if (m.find()) {
				// MD5
			}
			log.debug("Unable to find Date in file name {} with pattern", fileName);
		}
		catch (final ParseException e) {
			log.error("Failed to find date in string " + fileName, e);
		}
		return null;
	}

	@Override
	public String getMD5(final String fileName) {

		final Matcher m = PARTS_PATTERN.matcher(fileName);
		if (m.find()) {
			// Date
		}
		if (m.find()) {
			final String md5 = m.group(1);
			log.debug("Matched pattern group, MD5 is {}", md5);
			return md5;
		}
		log.debug("Unable to find MD5 in file name {} with pattern", fileName);
		return null;
	}
}
