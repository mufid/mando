package com.mando;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.SettingsController;

public class SettingsCommandToggle extends SherlockActivity {

    public void recheckButtonMode(View but) {

        final SettingsController s = new SettingsController(this);
        Integer i = (Integer) but.getTag();
        CompoundButton b = (CompoundButton) but;
        if (s.getCommandActive(i)) {
            b.setChecked(true);
            // but.setText(R.string.aktif);
        } else {
            b.setChecked(false);
            // but.setText(R.string.aktif_tidak);
        }

        // TODO : Masih belum yakin, tolong dicek lagi.
        if (s.getFailureMessage(i) != null) {
            b.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_settings_commandtoggle);
        TableLayout tl = (TableLayout) findViewById(R.id.commandtoggle_list);
        final SettingsController s = new SettingsController(this);
        final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        OnCheckedChangeListener toggleCommand = new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton but, boolean isChecked) {
                Button b = (Button) but;
                Integer i = (Integer) b.getTag();
                boolean newState = !s.getCommandActive(i);
                String failureMessage = s.getFailureMessage(i);
                if (failureMessage != null && newState) {
                    but.setChecked(false);
                    isChecked = false;
                    Toast.makeText(getApplicationContext(), failureMessage,
                            Toast.LENGTH_LONG).show();
                } else {
                    s.setCommandActive(i, newState);
                }
            }
        };

        OnClickListener customcommand = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TextView tv = (TextView) arg0;
                Integer commandID = (Integer) tv.getTag();

                Intent i = new Intent(getApplicationContext(),
                        SettingsCommandCustom.class);
                i.putExtra("id", commandID);
                Log.e("mando", "Opening. Giving id of " + commandID);
                startActivity(i);
            }
        };

        for (Pair<Integer, String> x : s.getCommandName()) {
            View row = inflater.inflate(R.layout.inline_toggle_row, null);
            TextView tv = ((TextView) row
                    .findViewById(R.id.inline_togglerow_commandname));
            CompoundButton but = ((CompoundButton) row
                    .findViewById(R.id.inline_togglerow_togglebutton));
            tv.setText(x.second);
            tv.setTag(x.first);
            but.setTag(x.first);
            recheckButtonMode(but);
            but.setOnCheckedChangeListener(toggleCommand);
            tl.addView(row);
            tv.setOnClickListener(customcommand);
        }

    }

    // TODO : Masih belum yakin, tolong dicek lagi.
    protected void onRestart() {
        super.onRestart();

        onCreate(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
