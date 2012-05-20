package com.morgan.design.seamlessbackup.service;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.UserDictionary;

import com.google.common.collect.Lists;
import com.morgan.design.seamlessbackup.domain.DictionaryWord;
import com.morgan.design.seamlessbackup.util.Logger;

public class WordDictionaryContentLoader extends AbstractContentLoader implements ContentLoader<List<DictionaryWord>> {

	// The user dictionary content URI
	private static final Uri CONTENT_URI = UserDictionary.Words.CONTENT_URI;
	private static final String DEFAULT_SORT_ORDER = UserDictionary.Words.DEFAULT_SORT_ORDER;

	private static final List<DictionaryWord> NONE_FOUND = Lists.newArrayList();

	//@formatter:off
	private static String[] WORD_COLUMNS = { 
		UserDictionary.Words._ID, 
		UserDictionary.Words.WORD, 
		UserDictionary.Words.LOCALE, 
		UserDictionary.Words.FREQUENCY
	};

	private static String[] ID_ONLY_COLUMN = { 
		UserDictionary.Words._ID
	};
	//@formatter:on

	@Override
	public List<DictionaryWord> loadContent(Context context) {
		Cursor mCursor = null;
		try {
			//@formatter:off
			mCursor = context.getContentResolver().query(
					CONTENT_URI, 
					WORD_COLUMNS, // The columns to return for each row
					null, // Either null, or the word the user entered
					null, // Either empty/null, or the string the user entered
					DEFAULT_SORT_ORDER);
			//@formatter:on
		}
		catch (Exception e) {
			Logger.e(TAG, "Exception thrown looking up dictionay content", e);
			return NONE_FOUND;
		}

		if (null == mCursor) {
			// Some providers return null if an error occurs, others throw an exception
			Logger.i(TAG, "Possible Error, cursor is null, no dictionary entries found");
			return NONE_FOUND;
		}
		else if (mCursor.getCount() < 1) {
			Logger.i(TAG, "No dictionary entries found");
			return NONE_FOUND;
		}
		else {
			Logger.i(TAG, String.format("Found %s entries", mCursor.getCount()));

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

	@Override
	public void updateContent(List<DictionaryWord> content, Context context) {

		ContentResolver contentResolver = context.getContentResolver();

		for (DictionaryWord dictionaryWord : content) {

			String mSelectClause = UserDictionary.Words.WORD + " = ?";
			String[] mSelectArgs = { dictionaryWord.getWord() };

			Cursor matchedWord = contentResolver.query(CONTENT_URI, ID_ONLY_COLUMN, mSelectClause, mSelectArgs, DEFAULT_SORT_ORDER);

			if (null == matchedWord) {
				Logger.i(TAG, "Possible Error, cursor is null, no dictionary entries found");
			}
			// No match, insert new row
			else if (0 == matchedWord.getCount()) {
				Logger.i(TAG, "Inserting New Word: " + dictionaryWord.getWord());
				contentResolver.insert(CONTENT_URI, createStatementValues(dictionaryWord));
			}
			// Found direct match
			else if (1 == matchedWord.getCount()) {
				Logger.i(TAG, "Found matching word, updating: " + dictionaryWord.getWord());

				matchedWord.moveToFirst();

				int idIndex = matchedWord.getColumnIndex(UserDictionary.Words._ID);
				int idOfMatchedWord = matchedWord.getInt(idIndex);

				// Defines selection criteria for the rows you want to update
				String mUpdateClause = UserDictionary.Words._ID + " = ?";
				String[] mUpdateArgs = { Integer.toString(idOfMatchedWord) };

				int mRowsUpdated = contentResolver.update(CONTENT_URI, createStatementValues(dictionaryWord), mUpdateClause, mUpdateArgs);
				Logger.i(TAG, "Dictionary Rows updated: " + mRowsUpdated);
			}
			else {
				// Multiple matches, TODO handle this?
			}
		}
	}

	private ContentValues createStatementValues(DictionaryWord dictionaryWord) {
		ContentValues mInsertValues = new ContentValues();
		mInsertValues.put(UserDictionary.Words.WORD, dictionaryWord.getWord());
		mInsertValues.put(UserDictionary.Words.LOCALE, dictionaryWord.getLocale());
		mInsertValues.put(UserDictionary.Words.FREQUENCY, dictionaryWord.getFrequency() + 1);
		return mInsertValues;
	}
}
