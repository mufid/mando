package com.mando.activities;

import java.util.Date;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.SettingsController;
import com.mando.helper.SettingsHelper;
import com.mando.service.MainService.LocalBinder;

import com.mando.R;

public class SettingsWelcome extends SherlockActivity {

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get
            // LocalService instance
            LocalBinder binder = (LocalBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_settings_welcome);

        // Service start
        // Intent intent = new Intent(this, MainService.class);
        // bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        SettingsHelper.init(this);

        // Kalau tombol dipencet, laksanakan kawan
        Button changePin = (Button) findViewById(R.id.welcome_buttonPinConf);
        Button commands = (Button) findViewById(R.id.welcome_buttonCommandConf);

        changePin.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        SettingsPINChange.class);
                // Set the request code to any code you like, you can identify
                // the
                // callback via this code
                startActivity(i);
            }
        });

        commands.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        SettingsCommandToggle.class);
                startActivity(i);
            }
        });

        Button test = (Button) findViewById(R.id.welcome_buttonTest);
        // Jika debug mode, hapus saja ini
        if (SettingsHelper.isDebug() == false) {
            test.setVisibility(View.GONE);
        }
        test.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        SettingsTest.class);
                startActivity(i);
            }
        });

        SettingsController settings = new SettingsController(this);
        if (settings.getCurrentPIN(true) == null) {
            Intent i = new Intent(getApplicationContext(),
                    SettingsPINChange.class);
            startActivityForResult(i, 0);
        }

    }

    @Override
    protected void onDestroy() {
        SettingsHelper.init(this);
        SettingsHelper.store("terakhir", new Date().toString());
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != 1) {
            SettingsController settings = new SettingsController(this);
            if (settings.getCurrentPIN(true) == null) {
                Intent i = new Intent(getApplicationContext(),
                        SettingsPINChange.class);
                startActivityForResult(i, 0);
            }
        }
    }

}
