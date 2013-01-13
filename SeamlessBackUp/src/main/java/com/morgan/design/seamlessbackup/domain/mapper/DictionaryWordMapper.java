package com.morgan.design.seamlessbackup.domain.mapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;
import android.provider.UserDictionary;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;

public class DictionaryWordMapper extends AbstractMappingHelper {

	private static Logger log = LoggerFactory.getLogger(DictionaryWordMapper.class);

	public List<DictionaryWord> mapCursor(Cursor mCursor) {

		List<DictionaryWord> foundConent = Lists.newArrayList();

		// Move cursor on, default is -1
		while (mCursor.moveToNext()) {

			DictionaryWord wordFound = new DictionaryWord();
			wordFound.setId(getInt(mCursor, UserDictionary.Words._ID));
			wordFound.setFrequency(getInt(mCursor, UserDictionary.Words.FREQUENCY));
			wordFound.setLocale(getString(mCursor, UserDictionary.Words.LOCALE));
			wordFound.setWord(getString(mCursor, UserDictionary.Words.WORD));

			log.debug("==================================");
			log.debug("Created DictionaryWord=[{}]", wordFound);
			log.debug("==================================");

			foundConent.add(wordFound);
		}

		return foundConent;
	}
}
