package com.mando.service;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class MainService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MainService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MainService.this;
        }
    }
    private MandoSMSReceiver mSMSreceiver;
	private IntentFilter mIntentFilter;

    @Override
    public IBinder onBind(Intent intent) {
    	if (false) {
    	Log.i("aku", "akuuuuuuu");
        mIntentFilter = new IntentFilter();
        mSMSreceiver = new MandoSMSReceiver();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mIntentFilter);
        Log.i("aku", "Nah berhasil"); }
        return mBinder;
    }

	/*
    private MandoSMSReceiver mSMSreceiver;
    private IntentFilter mIntentFilter;
	
    @Override
    public void onCreate()
    {
    	Log.i("aku suka padamu", "jangan jangan");
        super.onCreate();

        //SMS event receiver
        mSMSreceiver = new MandoSMSReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mIntentFilter);
        
        Log.i("aku suka padamu", "iya");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // Unregister the SMS receiver
        unregisterReceiver(mSMSreceiver);
    }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
*/
}