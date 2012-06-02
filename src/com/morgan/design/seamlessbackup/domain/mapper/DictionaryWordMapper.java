package com.morgan.design.seamlessbackup.domain.mapper;

import java.util.List;

import android.database.Cursor;
import android.provider.UserDictionary;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;
import com.morgan.design.seamlessbackup.util.Logger;

public class DictionaryWordMapper extends AbstractMappingHelper {

	public static final String TAG = "DictionaryWordMapper";

	public List<DictionaryWord> mapCursor(Cursor mCursor) {

		List<DictionaryWord> foundConent = Lists.newArrayList();

		// Move cursor on, default is -1
		while (mCursor.moveToNext()) {

			DictionaryWord wordFound = new DictionaryWord();
			wordFound.setId(getInt(mCursor, UserDictionary.Words._ID));
			wordFound.setFrequency(getInt(mCursor, UserDictionary.Words.FREQUENCY));
			wordFound.setLocale(getString(mCursor, UserDictionary.Words.LOCALE));
			wordFound.setWord(getString(mCursor, UserDictionary.Words.WORD));

			Logger.d(TAG, "==================================");
			Logger.d(TAG, wordFound);
			Logger.d(TAG, "==================================");

			foundConent.add(wordFound);
		}

		return foundConent;
	}
}
