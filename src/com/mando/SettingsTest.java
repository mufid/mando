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
import com.mando.helper.CallbackLocation;
import com.mando.helper.SMS;
import com.mando.service.MandoController;

// Implement LocationListener, biar bisa update lokasi.
public class SettingsTest extends SherlockActivity {

    CallbackLocation locationListener;

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
        locationListener = new CallbackLocation(this, locationManager);

        // End setup.
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                    int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                if (item.equals(getResources().getStringArray(
                        R.array.testing_menu_strings)[0])) {
                    // MandoController.getLocation(locationManager,
                    // locationListener);

                } else if (item.equals(getResources().getStringArray(
                        R.array.testing_menu_strings)[1])) {
                    SMS sepuluhSMS[] = MandoController.getSMS(10,
                            getApplicationContext());
                    for (int i = 0; i < sepuluhSMS.length; i++) {
                        String teks = sepuluhSMS[i].getNumber() + " :\n"
                                + sepuluhSMS[i].getMessage();
                        Toast.makeText(getApplicationContext(), teks, 1).show();
                    }
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
