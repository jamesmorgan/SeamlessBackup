package com.morgan.design.seamlessbackup.domain;

import java.io.File;

public class FileNameGenerator {

	public static String generateUploadPath(BackupType mBackupType, File mFile, DateTimeService dateTimeService) {

		String datetimeStamp = Constants.DATE_TIME_FORMAT.format(dateTimeService.getDate());

		return mBackupType.dir() + datetimeStamp + mFile.getName();
	}

}
