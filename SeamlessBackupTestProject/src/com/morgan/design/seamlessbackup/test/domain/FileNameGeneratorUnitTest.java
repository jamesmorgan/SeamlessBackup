package com.morgan.design.seamlessbackup.test.domain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.morgan.design.seamlessbackup.domain.BackupType;
import com.morgan.design.seamlessbackup.naming.BasicFileNameGenerator;
import com.morgan.design.seamlessbackup.naming.FileNameGenerator;
import com.morgan.design.seamlessbackup.service.DateTimeService;

public class FileNameGeneratorUnitTest {

	DateTimeService dateTimeService;
	FileNameGenerator fileNameGenerator = new BasicFileNameGenerator(dateTimeService);

	@Before
	public void setUp() {
		dateTimeService = new DateTimeService() {
			@Override
			@SuppressWarnings("deprecation")
			public Date getDate() {
				return new Date(2012, 06, 05, 10, 30, 01);
			}
		};
	}

	@Test
	public void shouldGenerateDateInCorrectStringFormat() {
		File file = null;
		try {
			file = new File("some-file.json");
			final String generatedPath = fileNameGenerator.generateUploadPath(BackupType.DICTIONARY, file);
			assertThat(generatedPath, is("/Dictionary/05073912:103001-some-file.json"));
		}
		finally {
			if (null != file) {
				file.delete();
			}
		}

	}
}
