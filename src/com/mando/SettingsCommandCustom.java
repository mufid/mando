package com.mando;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

public class SettingsCommandCustom extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
	}
	
	@Override
	protected void onDestroy() {
		
	}
}
