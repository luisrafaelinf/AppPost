package com.itla.apppost.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.itla.apppost.R;
import com.itla.apppost.constant.SessionManager;

public final class UtilsApp {

	public static String name(Context context) {
		return context.getString(R.string.app_name);
	}

	public static String token(Context context) {

		String name = name(context);

		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		String token = sharedPreferences.getString(SessionManager.TOKEN, "");

		return token;
	}

	public static Integer userId(Context context) {

		String name = name(context);

		SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		Integer userId = sharedPreferences.getInt(SessionManager.USER_ID, 0);

		return userId;
	}

	public static String uriApi(Context context) {
		return context.getResources().getString(R.string.URL_API);
	}

}
