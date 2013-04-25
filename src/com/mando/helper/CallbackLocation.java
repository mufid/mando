package com.mando.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class CallbackLocation implements Callback, LocationListener {

    private Context c;
    private LocationManager lm;

    public CallbackLocation(Context c, LocationManager lm) {
        this.c = c;
        this.lm = lm;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Context context = c;
        int duration = Toast.LENGTH_SHORT;

        String text = "Latitude : " + location.getLatitude() + "\nLongitude : "
                + location.getLongitude() + "\nAccuracy : "
                + location.getAccuracy() + "m";
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        lm.removeUpdates(this);
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

    @Override
    public void onSuccess() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onFailure() {
        // TODO Auto-generated method stub
        
    }

}
