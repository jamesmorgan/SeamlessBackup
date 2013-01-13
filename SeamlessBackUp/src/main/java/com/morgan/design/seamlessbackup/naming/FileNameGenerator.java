package com.morgan.design.seamlessbackup.naming;

import java.io.File;

import com.morgan.design.seamlessbackup.domain.BackupType;

public interface FileNameGenerator {

	String generateUploadPath(BackupType mBackupType, File mFile);

}
