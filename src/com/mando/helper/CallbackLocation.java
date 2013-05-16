package com.mando.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public abstract class CallbackLocation extends Callback {

    public CallbackLocation(Context c, String pn) {
        super(c, pn);
    }
    
    public abstract void onSuccess(String locationLat);
    public abstract void onSuccess(String locationLat, String locationName);
}
