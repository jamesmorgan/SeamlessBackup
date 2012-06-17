package com.morgan.design.seamlessbackup.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.dropbox.client2.DropboxAPI.Entry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileNameDateSorterUnitTest {

	@Test
	public void shouldParseDateCorrectly() throws ParseException {
		// Sat Jun 16 09:15:34 BST 2012
		assertThat(Constants.DATE_TIME_FORMAT.parse("16062012091534"), is(new GregorianCalendar(2012, 5, 16, 9, 15, 34).getTime()));

		// Sun Jun 17 07:24:30 BST 2012
		assertThat(Constants.DATE_TIME_FORMAT.parse("17062012072430"), is(new GregorianCalendar(2012, 5, 17, 7, 24, 30).getTime()));

		// Fri Jun 15 09:15:11 BST 2012
		assertThat(Constants.DATE_TIME_FORMAT.parse("15062012091511"), is(new GregorianCalendar(2012, 5, 15, 9, 15, 11).getTime()));
	}

	@Test
	public void shouldReturnUnChangedListWhenOnlyOneElement() {
		List<Entry> entries = Lists.newArrayList();
		entries.add(getEntry("16062012091534-dictionary.json"));
		FileNameDateSorter.sortByDate(entries);
		assertThat(entries.size(), is(1));
		assertThat(entries.get(0).fileName(), is("16062012091534-dictionary.json"));
	}

	@Test
	public void shouldSortDatesCorrectly() {
		List<Entry> entries = Lists.newArrayList();
		entries.add(getEntry("16062012091534-dictionary.json"));
		entries.add(getEntry("15062012091511-dictionary.json"));
		entries.add(getEntry("17062012072430-dictionary.json"));
		entries.add(getEntry("20062012072430-dictionary.json"));

		FileNameDateSorter.sortByDate(entries);

		assertThat(entries.size(), is(4));
		assertThat(entries.get(0).fileName(), is("20062012072430-dictionary.json"));
		assertThat(entries.get(1).fileName(), is("17062012072430-dictionary.json"));
		assertThat(entries.get(2).fileName(), is("16062012091534-dictionary.json"));
		assertThat(entries.get(3).fileName(), is("15062012091511-dictionary.json"));
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

	private Entry getEntry(String fileName) {
		Map<String, Object> fileParams = Maps.newHashMap();
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
