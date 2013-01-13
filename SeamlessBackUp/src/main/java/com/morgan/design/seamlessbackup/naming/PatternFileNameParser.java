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
	//@formatter:on

	@Override
	public Date getDate(final String fileName) {
		try {
			final Matcher matcher = PARTS_PATTERN.matcher(fileName);

			if (matcher.matches()) {
				final String dateString = matcher.group(0);
				log.debug("Matched pattern group, Date is {}", dateString);

				return Constants.DATE_TIME_FORMAT.parse(dateString);
			}
			log.debug("Unable to find Date in file name {} with pattern", fileName);
		}
		catch (final ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getMD5(final String fileName) {
		final Matcher matcher = PARTS_PATTERN.matcher(fileName);

		if (matcher.matches()) {
			final String md5String = matcher.group(1);
			log.debug("Matched pattern group, MD5 is {}", md5String);

			return md5String;
		}

		log.debug("Unable to find MD5 in file name {} with pattern", fileName);
		return null;
	}
}
