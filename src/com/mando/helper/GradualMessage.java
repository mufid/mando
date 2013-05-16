package com.mando.helper;

import java.util.ArrayList;

import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class GradualMessage {
    private String phoneNumber;
    private StringBuilder currentMessage;
    public GradualMessage(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.currentMessage = new StringBuilder();
    }
    public boolean addGradualMessage(String msg) {
        // Jika melebihi batas, maka split
        int nextLength = msg.length() + currentMessage.length();
        if (nextLength > 800 && nextLength < 1600) {
            sendSMS();
            currentMessage = new StringBuilder();
            currentMessage.append(msg);
            return true;
        } else if (nextLength > 1600) {
            sendSMS();
            currentMessage = new StringBuilder();
            currentMessage.append(msg.substring(0, 800));
            addGradualMessage(msg.substring(800));
        } else {
            currentMessage.append(msg);
        }
        return true;
    }
    private void sendSMS() {
        String phoneNumber = this.phoneNumber;
        String message = this.currentMessage.toString();

        SmsManager smsManager = SmsManager.getDefault();
        Log.i("mando", "Sending " + message.length());
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null,
                null);
    }
    public void flush() {
        if (this.currentMessage.length() > 0) {
            sendSMS();
        }
        currentMessage = new StringBuilder();
    }
}