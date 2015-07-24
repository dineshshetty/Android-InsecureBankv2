package com.android.insecurebankv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Base64;

/*
The class that handles the broadcast receiver functionality in the application.
When a change password is successful, a SMS is sent as a confirmation to the phone
number used by the user
@author Dinesh Shetty
*/
public class MyBroadCastReceiver extends BroadcastReceiver {
	String usernameBase64ByteString;
	public static final String MYPREFS = "mySharedPreferences";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

        String phn = intent.getStringExtra("phonenumber");
        String newpass = intent.getStringExtra("newpass");

		if (phn != null) {
			try {
                SharedPreferences settings = context.getSharedPreferences(MYPREFS, Context.MODE_WORLD_READABLE);
                final String username = settings.getString("EncryptedUsername", null);
                byte[] usernameBase64Byte = Base64.decode(username, Base64.DEFAULT);
                usernameBase64ByteString = new String(usernameBase64Byte, "UTF-8");
                final String password = settings.getString("superSecurePassword", null);
                CryptoClass crypt = new CryptoClass();
                String decryptedPassword = crypt.aesDeccryptedString(password);
                String textPhoneno = phn.toString();
                String textMessage = "Updated Password from: "+decryptedPassword+" to: "+newpass;
                SmsManager smsManager = SmsManager.getDefault();
                System.out.println("For the changepassword - phonenumber: "+textPhoneno+" password is: "+textMessage);
                smsManager.sendTextMessage(textPhoneno, null, textMessage, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        else {
            System.out.println("Phone number is null");
        }
	}

}