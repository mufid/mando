package com.mando;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.service.MandoController;

// Implement LocationListener, biar bisa update lokasi.
public class SettingsTest extends SherlockActivity implements LocationListener {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock_Light);
		setContentView(R.layout.activity_settings_test);

		ListView lv = (ListView) findViewById(R.id.listView1);

		String[] values = getResources().getStringArray(
				R.array.testing_menu_strings);

		// Setup lokasi.
		final LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		// Tiap 5 menit sekali, update lokasi.
		final long updateInterval = 1000 * 5 * 60;
		List<String> providers = locationManager.getAllProviders();
		for (int i = 0; i < providers.size(); i++) {
			String provider = providers.get(i);
			locationManager.requestLocationUpdates(provider, updateInterval, 0,
					this);
		}
		// End setup.

		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values));

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				String item = (String) parent.getItemAtPosition(position);
				if (item.equals("Lokasi")) {
					Context context = getApplicationContext();
					int duration = Toast.LENGTH_LONG;

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

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
