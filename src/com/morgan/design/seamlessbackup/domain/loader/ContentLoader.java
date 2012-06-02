package com.morgan.design.seamlessbackup.domain.loader;

import android.content.Context;

public interface ContentLoader<T> {

	T loadContent(Context context);

	void updateContent(T content, Context context);
}
