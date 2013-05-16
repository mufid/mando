package com.mando;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.mando.helper.Callback;
import com.mando.helper.CallbackLocation;
import com.mando.helper.LocationHelper;
import com.mando.helper.SMS;
import com.mando.helper.SettingsHelper;
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
        //locationListener = new CallbackLocation(this, locationManager);

        // End setup.
        lv.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                    int position, long id) {
                String item = (String) parent.getItemAtPosition(position);

                MandoController.c = getApplicationContext();

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
                } else if (item.equals(getResources().getStringArray(
                        R.array.testing_menu_strings)[2])) {
                    Toast.makeText(getApplicationContext(),
                            MandoController.getContacts("Fi "), 1).show();
                } else if (item.equals(getResources().getStringArray(
                        R.array.testing_menu_strings)[3])) {
                    
                    final Handler handler = new Handler() {
                        public void handleMessage(Message msg) {
                              if(msg.arg1 == 1)
                                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                        }
                    };
                    
                    LocationHelper.getLocationName("-5.280", "106.386", new Callback(null, null) {
                        
                        @Override
                        public void onSuccess(String successMessage) {
                            Message msg = new Message();
                            msg.arg1 = 1;
                            msg.obj = successMessage;
                            handler.sendMessage(msg);
                        }
                        
                        @Override
                        public void onFailure() { }
                    });

                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
