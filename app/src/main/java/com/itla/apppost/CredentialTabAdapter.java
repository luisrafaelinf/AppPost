package com.itla.apppost;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.itla.apppost.credential.LoginActivity;
import com.itla.apppost.credential.RegisterActivity;

public final class CredentialTabAdapter extends FragmentPagerAdapter {

	private final LoginActivity login = new LoginActivity();
	private final RegisterActivity register = new RegisterActivity();


	private int totalTabs;

	public CredentialTabAdapter(@NonNull FragmentManager fm, int behavior) {
		super(fm, behavior);

		totalTabs = behavior;
	}

	@NonNull
	@Override
	public Fragment getItem(int position) {

		switch (position) {

			case 0:
				return login;

			case 1:
				return register;

		}

		return null;
	}

	@Override
	public int getCount() {
		return totalTabs;
	}
}
