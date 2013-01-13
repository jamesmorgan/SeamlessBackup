package com.morgan.design.seamlessbackup.naming;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.dropbox.client2.DropboxAPI.Entry;

public class DefaultFileNameSorter implements FileNameSorter {

	private final FileNameParser fileNameParser;

	public DefaultFileNameSorter(final FileNameParser fileNameParser) {
		this.fileNameParser = fileNameParser;
	}

	@Override
	public List<Entry> sortByDate(final List<Entry> contents) {
		if (1 == contents.size()) {
			return contents;
		}
		Collections.sort(contents, new Comparator<Entry>() {
			@Override
			public int compare(final Entry lhs, final Entry rhs) {
				final Date lhsDate = fileNameParser.getDate(lhs.fileName());
				final Date rhsDate = fileNameParser.getDate(rhs.fileName());

				return lhsDate.compareTo(rhsDate);
			}
		});

		Collections.reverse(contents);

		return contents;
	}

}
