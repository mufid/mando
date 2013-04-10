package com.mando;


import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.SettingsController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

public class SettingsCommandToggle extends SherlockActivity {

    public void recheckButtonMode(Button but) {
        final SettingsController s = new SettingsController(this);
        Integer i = (Integer) but.getTag();
        if (s.getCommandActive(i)) {
            but.setText(R.string.aktif);
        } else {
            but.setText(R.string.aktif_tidak);
        }
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_settings_commandtoggle);
		TableLayout tl = (TableLayout) findViewById(R.id.commandtoggle_list);
		final SettingsController s = new SettingsController(this);
		final Context context = getApplicationContext();
		final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		OnClickListener toggleCommand = new OnClickListener() {
			@Override
			public void onClick(View but) {
				Button b = (Button) but;
				Integer i = (Integer) b.getTag();
				s.setCommandActive(i, !s.getCommandActive(i));
				recheckButtonMode(b);
			}
		};
		
		OnClickListener customcommand = new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TextView tv = (TextView) arg0;
                Integer commandID = (Integer) tv.getTag();
                
                Intent i = new Intent(getApplicationContext(), SettingsCommandCustom.class);
                i.putExtra("id", commandID);
                startActivity(i);
            }
		};

		for (Pair<Integer, String> x : s.getCommandName()) {
	        View row = inflater.inflate(R.layout.inline_toggle_row, null);
	        TextView tv = ((TextView)row.findViewById(R.id.inline_togglerow_commandname));
	        Button but = ((Button) row.findViewById(R.id.inline_togglerow_togglebutton));
	        tv.setText(x.second);
	        tv.setTag(x.first);
	        but.setTag(x.first);
	        but.setOnClickListener(toggleCommand);
	        recheckButtonMode(but);
	        tl.addView(row);
	        tv.setOnClickListener(customcommand);
        }
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
