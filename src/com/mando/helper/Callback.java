package com.mando.helper;

import java.util.ArrayList;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Kelas ini digunakan untuk keperluan yang asinkron,
 * seperti misalnya merekam suara
 * @author Mufid
 *
 */
public abstract class Callback {
    protected Context c;
    private String phoneNum;
    public Callback(Context c, String pn) {
        this.c = c;
        this.phoneNum = pn;
    }
    protected void send(String message) {
        String phoneNumber = this.phoneNum;

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
                null);
    }
	public abstract void onSuccess();
	public abstract void onFailure();
}
