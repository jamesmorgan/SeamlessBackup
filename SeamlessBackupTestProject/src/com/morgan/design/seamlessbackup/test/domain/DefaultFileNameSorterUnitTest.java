package com.morgan.design.seamlessbackup.test.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.dropbox.client2.DropboxAPI.Entry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.morgan.design.seamlessbackup.domain.Constants;
import com.morgan.design.seamlessbackup.naming.DefaultFileNameSorter;
import com.morgan.design.seamlessbackup.naming.PatternFileNameParser;

public class DefaultFileNameSorterUnitTest {

	private final DefaultFileNameSorter defaultFileNameSorter = new DefaultFileNameSorter(new PatternFileNameParser());

	@Test
	public void shouldParseDateCorrectly() throws Exception {
		// Sat Jun 16 09:15:34 BST 2012
		assertThat(Constants.DATE_TIME_FORMAT.parse("16062012:091534"),
				is(new GregorianCalendar(2012, 5, 16, 9, 15, 34).getTime()));

		// Sun Jun 17 07:24:30 BST 2012
		assertThat(Constants.DATE_TIME_FORMAT.parse("17062012:072430"),
				is(new GregorianCalendar(2012, 5, 17, 7, 24, 30).getTime()));

		// Fri Jun 15 09:15:11 BST 2012
		assertThat(Constants.DATE_TIME_FORMAT.parse("15062012:091511"),
				is(new GregorianCalendar(2012, 5, 15, 9, 15, 11).getTime()));
	}

	@Test
	public void shouldReturnUnChangedListWhenOnlyOneElement() {
		final List<Entry> entries = Lists.newArrayList();
		entries.add(getEntry("16062012:091534-dictionary.json"));
		new DefaultFileNameSorter(new PatternFileNameParser()).sortByDate(entries);
		assertThat(entries.size(), is(1));
		assertThat(entries.get(0).fileName(), is("16062012:091534-dictionary.json"));
	}

	@Test
	public void shouldSortDatesCorrectly() {
		final List<Entry> entries = Lists.newArrayList();
		entries.add(getEntry("16062012:091534-dictionary.json"));
		entries.add(getEntry("15062012:091511-dictionary.json"));
		entries.add(getEntry("17062012:072430-dictionary.json"));
		entries.add(getEntry("20062012:072430-dictionary.json"));

		defaultFileNameSorter.sortByDate(entries);

		assertThat(entries.size(), is(4));
		assertThat(entries.get(0).fileName(), is("20062012:072430-dictionary.json"));
		assertThat(entries.get(1).fileName(), is("17062012:072430-dictionary.json"));
		assertThat(entries.get(2).fileName(), is("16062012:091534-dictionary.json"));
		assertThat(entries.get(3).fileName(), is("15062012:091511-dictionary.json"));
	}

	//@formatter:off
	//	{
	//	    "hash": "528dda36e3150ba28040052bbf1bfbd1",
	//	    "thumb_exists": false,
	//	    "bytes": 0,
	//	    "modified": "Sat, 12 Jan 2008 23:10:10 +0000",
	//	    "path": "/Public",
	//	    "is_dir": true,
	//	    "size": "0 bytes",
	//	    "root": "dropbox",
	//	    "contents": [
	//	    {
	//	        "thumb_exists": false,
	//	        "bytes": 4392763,
	//	        "modified": "Thu, 15 Jan 2009 02:52:43 +0000",
	//	        "path": "/Public/test_123_abc.zip",
	//	        "is_dir": false,
	//	        "icon": "page_white_compressed",
	//	        "size": "4.2MB"
	//	    }
	//	    ],
	//	    "icon": "folder_public"
	//	 }
	//@formatter:on

	private Entry getEntry(final String fileName) {
		final Map<String, Object> fileParams = Maps.newHashMap();
		fileParams.put("hash", "528dda36e3150ba28040052bbf1bfbd1");
		fileParams.put("thumb_exists", false);
		fileParams.put("bytes", 4392763);
		fileParams.put("modified", "Thu, 15 Jan 2009 02:52:43 +0000");
		fileParams.put("is_dir", false);
		fileParams.put("icon", "page_white_compressed");
		fileParams.put("size", "4.2MB");
		fileParams.put("path", fileName);
		fileParams.put("root", "dropbox");
		fileParams.put("icon", "folder_public");

		return new Entry(fileParams);
	}
}
