package com.mando;

import java.util.Date;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.SettingsHelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SettingsWelcome extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_welcome);
		
		SettingsHelper.init(this);
		String terakhir = SettingsHelper.read("terakhir");
		String teks;
		
		if (terakhir == null) {
			teks = getString(R.string.setting_terakhir_tidakpernah);
		} else {
			teks = String.format(getString(R.string.setting_terakhir), terakhir.toString());
		}
		
		TextView welcomeLabel = (TextView) findViewById(R.id.welcome_welcometext);
		welcomeLabel.setText(teks);
		
		SettingsHelper.store("terakhir", new Date().toString());
		
		// Kalau tombol dipencet, laksanakan kawan
		Button changePin = (Button) findViewById(R.id.welcome_buttonPinConf);
		Button commands = (Button) findViewById(R.id.welcome_buttonCommandConf);
		
		changePin.setOnClickListener(new OnClickListener() {			
			public void onClick(View arg0) {
				
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		
	}
}
