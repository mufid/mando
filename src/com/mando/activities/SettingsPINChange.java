package com.mando.activities;

import java.util.Hashtable;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mando.helper.SettingsController;

public class SettingsPINChange extends SherlockActivity {

    boolean adaPIN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_settings_pin);

        SettingsController settings = new SettingsController(this);
        adaPIN = (settings.getCurrentPIN(true) != null);

        if (!adaPIN) {
            TextView pinSaatIni = (TextView) findViewById(R.id.textView1);
            pinSaatIni.setText(getString(R.string.setting_pin_pertamakali));

            EditText editPinSaatIni = (EditText) findViewById(R.id.pin_current);
            editPinSaatIni.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(R.string.save)
                .setIcon(R.drawable.ic_ok)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    public String textOf(int r_id) {
        EditText e = (EditText) findViewById(r_id);
        return e.getText().toString();
    }

    public void setError(int r_id, String errorName) {
        EditText e = (EditText) findViewById(r_id);
        e.setError(errorName);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // Karena hanya ada satu menu, jadi kalau diklik
        SettingsController s = new SettingsController(this);

        String oldPin, newPin, newPin2;

        if (!adaPIN) {
            oldPin = s.getCurrentPIN();
        } else {
            oldPin = textOf(R.id.pin_current);
        }

        newPin = textOf(R.id.pin_new);
        newPin2 = textOf(R.id.pin_newagain);

        Hashtable<Integer, String> kesalahan = s.checkValidPIN(oldPin, newPin,
                newPin2);

        // Jika tidak ada kesalahan, maka simpan perubahan
        if (kesalahan.size() == 0) {
            s.setPIN(newPin);
            Toast.makeText(this, R.string.setting_pin_changed,
                    Toast.LENGTH_LONG).show();
            this.finish();
        }

        // Jika ada kesalahan, maka jangan simpan perubahan
        // dan tampilkan pesan kesalahan
        else {
            int[] errors = { R.id.pin_current, R.id.pin_new, R.id.pin_newagain };

            for (int i : errors) {
                String x = kesalahan.get(i);
                if (x == null)
                    setError(i, null);
                else
                    setError(i, x);
            }
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
