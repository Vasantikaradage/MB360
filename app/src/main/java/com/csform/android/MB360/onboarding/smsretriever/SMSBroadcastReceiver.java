package com.csform.android.MB360.onboarding.smsretriever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

            switch (status.getStatusCode()) {
                //status code indicates if we have a successful message retrieval
                case CommonStatusCodes.SUCCESS:
                    //get the message
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                    //TODO- Extract the otp from the message

                    break;

                case CommonStatusCodes.TIMEOUT:
                    //wait handler
                    //TODO - show the error for time out to user

            }
        }
    }
}
