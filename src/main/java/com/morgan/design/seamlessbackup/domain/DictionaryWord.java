package com.morgan.design.seamlessbackup.domain;

import com.google.common.base.Objects;

public class DictionaryWord {

	private int id;
	private String word;
	private String locale;
	private int frequency;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getLocale() {
		return this.locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("word", word).add("locale", locale).add("frequency", frequency).toString();
	}

}
