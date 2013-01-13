package com.morgan.design.seamlessbackup.adaptor.types;

import android.content.Context;

import com.morgan.design.seamlessbackup.domain.BackupType;

public interface SelectableBackupType {

	CharSequence getCheckboxName();

	CharSequence getCheckboxDescription(Context context);

	BackupType getBackupType();
}
