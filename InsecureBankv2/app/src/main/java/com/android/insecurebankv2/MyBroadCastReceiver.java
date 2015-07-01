package com.android.insecurebankv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/*
The class that handles the broadcast receiver functionality in the application.
When a transaction is successful, a SMS is sent as a confirmation to the phone
number entered by the user
@author Dinesh Shetty
*/
public class MyBroadCastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String phn = intent.getStringExtra("phonenumber");
		if (phn != null) {
			try {
				String textPhoneno = phn.toString();
				String textMessage = "Transaction Successful";
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(textPhoneno, null, textMessage, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}