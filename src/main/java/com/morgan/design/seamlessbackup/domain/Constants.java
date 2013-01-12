package com.morgan.design.seamlessbackup.domain;

import java.text.SimpleDateFormat;
import java.util.List;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.adaptor.types.BrowserBookmarksListViewSelectionType;
import com.morgan.design.seamlessbackup.adaptor.types.BrowserSearchHistoryListViewSelectionType;
import com.morgan.design.seamlessbackup.adaptor.types.DictionaryListViewSelectionType;
import com.morgan.design.seamlessbackup.adaptor.types.SelectableBackupType;

public class Constants {

	public static final boolean LOG_OUT = true;
	public static final boolean RETRY = true;

	public static final String DEFAULT_DATE_TIME_FORMAT = "ddMMyyyyHHmmss";
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);

	public static List<SelectableBackupType> backupTypes;
	static {
		backupTypes = Lists.newArrayList();
		backupTypes.add(new DictionaryListViewSelectionType());
		backupTypes.add(new BrowserBookmarksListViewSelectionType());
		backupTypes.add(new BrowserSearchHistoryListViewSelectionType());
	}

}
