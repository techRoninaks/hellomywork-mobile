package com.roninaks.hellomywork.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.roninaks.hellomywork.interfaces.SmsListener;

public class SmsReceiver extends BroadcastReceiver{
    private static SmsListener mListener;
    Boolean b;
    String abcd,xyz;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();

            String messageBody = smsMessage.getMessageBody();
            abcd=messageBody.replaceAll("[^0-9]","");
            if(b==true) {
                mListener.messageReceived(abcd);
            }
            else
            {
            }
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}