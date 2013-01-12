package com.morgan.design.seamlessbackup.adaptor.types;

import android.content.Context;

import com.morgan.design.seamlessbackup.domain.BackupType;

public class BrowserSearchHistoryListViewSelectionType implements SelectableBackupType {

	@Override
	public CharSequence getCheckboxName() {
		return BackupType.BROWSER_SEARCH_HISTORY.prettyName();
	}

	@Override
	public CharSequence getCheckboxDescription(Context context) {
		return context.getResources().getString(BackupType.BROWSER_SEARCH_HISTORY.getResourceDescriptionId());
	}

	@Override
	public BackupType getBackupType() {
		return BackupType.BROWSER_SEARCH_HISTORY;
	}
}
