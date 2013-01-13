package com.morgan.design.seamlessbackup.adaptor.types;

import android.content.Context;

import com.morgan.design.seamlessbackup.domain.BackupType;

public class DictionaryListViewSelectionType implements SelectableBackupType {

	private final BackupType backupType = BackupType.DICTIONARY;

	@Override
	public CharSequence getCheckboxName() {
		return backupType.prettyName();
	}

	@Override
	public CharSequence getCheckboxDescription(final Context context) {
		return context.getResources().getString(backupType.getResourceDescriptionId());
	}

	@Override
	public BackupType getBackupType() {
		return backupType;
	}

}
