package com.mando;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.SettingsController;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsCommandCustom extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_settings_commandcustom);
		
        Intent i = getIntent();
        SettingsController s = new SettingsController(this);

		int commandID = i.getExtras().getInt("id");
		EditText ed = ((EditText) findViewById(R.id.customcommand_edittext));
		ed.setText(s.getCommandString(commandID));
		TextView tv = (TextView) findViewById(R.id.customcommand_titlelabel);
		tv.setText(String.format(getString(R.string.setting_customcommandfor), s.getCommandName(commandID)));
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
	    if (!typedcommand.equals(currentcommand)) {
	        Toast.makeText(this, String.format(getString(R.string.setting_customcommand_changed), s.getCommandName(commandID)), Toast.LENGTH_LONG).show();
	    }
		super.onDestroy();
	}
}
