package com.morgan.design.seamlessbackup.domain;

import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.dropbox.client2.DropboxAPI.Entry;

public class FileNameDateSorter {

	public static List<Entry> sortByDate(List<Entry> contents) {
		if (1 == contents.size()) {
			return contents;
		}
		Collections.sort(contents, new Comparator<Entry>() {
			@Override
			public int compare(Entry lhs, Entry rhs) {
				Date lhsDate;
				lhsDate = getDate(lhs);
				Date rhsDate = getDate(rhs);
				return lhsDate.compareTo(rhsDate);
			}
		});

		Collections.reverse(contents);

		return contents;
	}

	public static Date getDate(Entry rhs) {
		String fileName = rhs.fileName();
		String dateString = fileName.split("-")[0];
		try {
			return Constants.DATE_TIME_FORMAT.parse(dateString);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
