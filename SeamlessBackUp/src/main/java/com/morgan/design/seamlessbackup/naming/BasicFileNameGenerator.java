package com.morgan.design.seamlessbackup.naming;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.domain.Constants;
import com.morgan.design.seamlessbackup.service.DateTimeService;
import com.morgan.design.seamlessbackup.util.MD5;

public class BasicFileNameGenerator implements FileNameGenerator {

	private final Logger log = LoggerFactory.getLogger(getClass());

	// TODO inject MD5 and DateTimeService

	private final DateTimeService dateTimeService;

	public BasicFileNameGenerator(final DateTimeService dateTimeService) {
		this.dateTimeService = dateTimeService;
	}

	@Override
	public String generateUploadPath(final BackupType mBackupType, final File file) {

		final String md5Hex = MD5.hash(file);
		log.debug("MD5 for file {} is {}", file.getName(), md5Hex);

		final String datetimeStamp = Constants.DATE_TIME_FORMAT.format(dateTimeService.getDate());
		log.debug("DateStamp for file {} is {}", file.getName(), datetimeStamp);

		return mBackupType.dir() + prepare(datetimeStamp) + "-" + prepare(md5Hex) + "-" + file.getName();
	}

	private static String prepare(final String data) {
		return "[" + data + "]";
	}

}
