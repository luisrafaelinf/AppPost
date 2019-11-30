package com.itla.apppost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

	private TabLayout tabCredentials;
	private ViewPager viewTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLocale();
		setContentView(R.layout.activity_main);

		tabCredentials = findViewById(R.id.tabCredentials);

		viewTab = findViewById(R.id.viewTab);

		createTabCredentials();

		viewTab.setAdapter(new CredentialTabAdapter(getSupportFragmentManager(), tabCredentials.getTabCount()));
		viewTab.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabCredentials));

		tabCredentials.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {

				viewTab.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void createTabCredentials() {
		tabCredentials.addTab(tabCredentials.newTab().setText("LOGIN"));
		tabCredentials.addTab(tabCredentials.newTab().setText("REGISTER"));
		tabCredentials.setTabGravity(TabLayout.GRAVITY_FILL);

	}

	private void setLocale() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-04:00"));
		Locale.setDefault(new Locale("es", "DO"));

	}
}
