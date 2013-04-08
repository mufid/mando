package com.mando;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.service.MandoController;

public class SettingsTest extends SherlockActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_settings_test);

		final LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		ListView lv = (ListView) findViewById(R.id.listView1);

		String[] values = getResources().getStringArray(
				R.array.testing_menu_strings);

		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values));

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (item.equals("Lokasi")) {
					Context context = getApplicationContext();
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context,
							MandoController.getLocation(locationManager),
							duration);
					toast.show();
				} else if (item.equals("SMS")) {

				}
			}

		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
