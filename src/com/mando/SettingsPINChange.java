package com.mando;


import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

public class SettingsPINChange extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_settings_pin);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
