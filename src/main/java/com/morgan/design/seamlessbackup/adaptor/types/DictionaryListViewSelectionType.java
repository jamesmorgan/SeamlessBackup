package com.morgan.design.seamlessbackup.adaptor.types;

import android.content.Context;

import com.morgan.design.seamlessbackup.domain.BackupType;

public class DictionaryListViewSelectionType implements SelectableBackupType {

	@Override
	public CharSequence getCheckboxName() {
		return BackupType.DICTIONARY.prettyName();
	}

	@Override
	public CharSequence getCheckboxDescription(Context context) {
		return context.getResources().getString(BackupType.DICTIONARY.getResourceDescriptionId());
	}

	@Override
	public BackupType getBackupType() {
		return BackupType.DICTIONARY;
	}

}
