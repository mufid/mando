package com.mando;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.SettingsController;

public class SettingsCommandCustom extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_settings_commandcustom);

        Intent i = getIntent();
        SettingsController s = new SettingsController(this);

        int commandID = i.getExtras().getInt("id");
        final EditText ed = ((EditText) findViewById(R.id.customcommand_edittext));
        ed.setText(s.getCommandString(commandID));
        TextView tv = (TextView) findViewById(R.id.customcommand_titlelabel);
        tv.setText(String.format(getString(R.string.setting_customcommandfor),
                s.getCommandName(commandID)));
        ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (commandStringValid(ed.getText().toString())) {
                    ed.setError(null);
                } else {
                    ed.setError("Perintah hanya boleh berisi angka dan huruf");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    boolean commandStringValid(String what) {
        return what.matches("^[0-9A-Za-z]+$");
    }

    @Override
    protected void onDestroy() {
        // Cek apakah ada perubah
        Intent i = getIntent();
        SettingsController s = new SettingsController(this);
        int commandID = i.getExtras().getInt("id");
        String currentcommand = s.getCommandString(commandID);
        EditText ed = ((EditText) findViewById(R.id.customcommand_edittext));
        String typedcommand = ed.getText().toString();
        if (!typedcommand.equals(currentcommand)
                && commandStringValid(typedcommand)) {
            s.setCommandString(commandID, typedcommand);
            Toast.makeText(
                    this,
                    String.format(
                            getString(R.string.setting_customcommand_changed),
                            s.getCommandName(commandID)), Toast.LENGTH_LONG)
                    .show();
        }
        super.onDestroy();
    }
}
