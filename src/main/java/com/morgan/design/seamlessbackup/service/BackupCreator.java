package com.morgan.design.seamlessbackup.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.morgan.design.seamlessbackup.domain.BackupType;

public class BackupCreator {

	private static Logger log = LoggerFactory.getLogger(BackupCreator.class);

	private static final String SEAMLESS_BACKUP_DIR = "/SeamlessBackup/";

	public File createFile(final List<?> loadContent, final BackupType type) {
		try {
			final Gson gson = new GsonBuilder().setPrettyPrinting().create();
			final String json = gson.toJson(loadContent);

			final File file = makeFile(type);
			final FileOutputStream fOut = new FileOutputStream(file);

			/*
			 * We have to use the openFileOutput()-method the ActivityContext
			 * provides, to protect your file from others and This is done for
			 * security-reasons. We chose MODE_WORLD_READABLE, because we have
			 * nothing to hide in our file
			 */
			final OutputStreamWriter osw = new OutputStreamWriter(fOut);

			// Write the string to the file
			osw.write(json);
			/*
			 * ensure that everything is really written out and close
			 */
			osw.flush();
			osw.close();

			return file;
		}
		catch (final FileNotFoundException e) {
			e.printStackTrace();
			log.error("Unable to create file for backup, file not found", e);
		}
		catch (final IOException e) {
			e.printStackTrace();
			log.error("Unable to create file for backup, IOException", e);
		}
		return null;
	}

	private File makeFile(final BackupType type) throws IOException {

		// ##### Write a DIR to the disk #####
		final File dir = new File(Environment.getExternalStorageDirectory(), SEAMLESS_BACKUP_DIR);
		if (!dir.exists()) {
			log.info("Making DIR as not proesent, file path: {}", dir.getPath());
			dir.mkdirs();
		}

		// ##### Write a FILE to the disk #####
		final File file = new File(dir, type.fileName());
		if (!file.exists()) {
			log.info("Making FILE as not proesent, file path: {}", file.getPath());
			file.createNewFile();
		}

		return file;
	}

}
