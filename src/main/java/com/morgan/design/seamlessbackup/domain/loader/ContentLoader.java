package com.morgan.design.seamlessbackup.domain.loader;

import java.util.List;

import android.content.Context;

public interface ContentLoader<T> {

	List<T> loadContent(Context context);

	void updateContent(List<T> content, Context context);
}
