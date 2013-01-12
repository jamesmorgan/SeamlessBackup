package com.morgan.design.seamlessbackup.domain;

import java.io.File;

import com.morgan.design.seamlessbackup.service.DateTimeService;

public class FileNameGenerator {

	public static String generateUploadPath(BackupType mBackupType, File mFile, DateTimeService dateTimeService) {

		String datetimeStamp = Constants.DATE_TIME_FORMAT.format(dateTimeService.getDate());

		return mBackupType.dir() + datetimeStamp + "-" + mFile.getName();
	}

}
