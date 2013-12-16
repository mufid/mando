package com.mando.service;

import com.mando.helper.SettingsHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MandoSMSReceiver extends BroadcastReceiver {
    // private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        String strMessage = "";

        if (extras != null) {
            Object[] smsextras = (Object[]) extras.get("pdus");

            for (int i = 0; i < smsextras.length; i++) {
                SmsMessage smsmsg = SmsMessage
                        .createFromPdu((byte[]) smsextras[i]);

                String strMsgBody = smsmsg.getMessageBody().toString();
                String strMsgSrc = smsmsg.getOriginatingAddress();
                strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;

                Log.i("mando", strMessage);
                if (SettingsHelper.isDebug())
                    Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();

                MandoController.processSMS(strMsgBody, strMsgSrc, context);
            }
            
        }
        
      
    }

}