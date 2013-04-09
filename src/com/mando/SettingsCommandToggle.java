package com.mando;


import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

public class SettingsCommandToggle extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_settings_commandtoggle);
	}
	
	@Override
	protected void onDestroy() {
		
	}
}
