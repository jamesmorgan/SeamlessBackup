package com.morgan.design.seamlessbackup.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;

import android.test.suitebuilder.annotation.SmallTest;

public class FileNameGeneratorUnitTest extends TestCase {

	DateTimeService dateTimeService;

	@Before
	@Override
	public void setUp() {
		dateTimeService = new DateTimeService() {
			@Override
			public Date getDate() {
				return new Date(2012, 06, 05, 10, 30, 01);
			}
		};
	}

	@SmallTest
	public void testMe() {
		File file = null;
		try {
			file = new File("some-file.json");
			String generatedPath = FileNameGenerator.generateUploadPath(BackupType.DICTIONARY, file, dateTimeService);
			assertThat(generatedPath, is("/Dictionary/05073912103001-some-file.json"));
		}
		finally {
			if (null != file) {
				file.delete();
			}
		}

	}
}
