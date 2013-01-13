package com.morgan.design.seamlessbackup.adaptor.types;

import android.content.Context;

import com.morgan.design.seamlessbackup.domain.BackupType;

public class BrowserSearchHistoryListViewSelectionType implements SelectableBackupType {

	private final BackupType backupType = BackupType.BROWSER_SEARCH_HISTORY;

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
