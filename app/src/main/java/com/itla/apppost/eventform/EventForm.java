package com.itla.apppost.eventform;

import androidx.annotation.NonNull;

public interface EventForm<T> {

	void fillViewContent(@NonNull T entity);
}
