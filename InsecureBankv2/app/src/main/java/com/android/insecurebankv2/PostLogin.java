package com.android.insecurebankv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import com.marcohc.toasteroid.Toasteroid;


/*
The page that allows gives the user below functionalities
Transfer: Module that allows transfer of amount between two accounts
View Statement: Module that allows the user to view transaction history for the logged in user
Change Password:  Module that allows the logged in user to change the password
@author Dinesh Shetty
*/
public class PostLogin extends Activity {
	//	The Button that handles the transfer activity
	Button transfer_button;
    //  The Textview that handles the root status display
	TextView root_status;
	//	The Button that handles the view transaction history activity
	Button statement_button;
	//	The Button that handles the change password activity
	Button changepasswd_button;
	String uname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_login);
		Intent intent = getIntent();
		uname = intent.getStringExtra("uname");

        root_status =(TextView) findViewById(R.id.rootStatus);
        //  Display root status
        showRootStatus();
        //	Display emulator status
        checkEmulatorStatus();

		transfer_button = (Button) findViewById(R.id.trf_button);
		transfer_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				The class that allows allows transfer of amount between two accounts
				*/
				Intent dT = new Intent(getApplicationContext(), DoTransfer.class);
				startActivity(dT);
			}
		});
		statement_button = (Button) findViewById(R.id.viewStatement_button);
		statement_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewStatment();
			}
		});
		changepasswd_button = (Button) findViewById(R.id.button_ChangePasswd);
		changepasswd_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changePasswd();
			}
		});
	}

	private void checkEmulatorStatus() {
		Boolean isEmulator = checkIfDeviceIsEmulator();
		if(isEmulator==true)
		{
			Toasteroid.show(this, "Application running on Emulator", Toasteroid.STYLES.ERROR, Toasteroid.LENGTH_LONG);
		}
		else
		{
			Toasteroid.show(this, "Application running on Real device", Toasteroid.STYLES.SUCCESS, Toasteroid.LENGTH_LONG);
		}
	}

	private Boolean checkIfDeviceIsEmulator() {
		if(Build.FINGERPRINT.startsWith("generic")
				|| Build.FINGERPRINT.startsWith("unknown")
				|| Build.MODEL.contains("google_sdk")
				|| Build.MODEL.contains("Emulator")
				|| Build.MODEL.contains("Android SDK built for x86")
				|| Build.MANUFACTURER.contains("Genymotion")
				|| (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
				|| "google_sdk".equals(Build.PRODUCT))
		{
			return true;
		}
		return false;
	}


	void showRootStatus() {
        boolean isrooted = doesSuperuserApkExist("/system/app/Superuser.apk")||
                doesSUexist();
        if(isrooted==true)
        {
            root_status.setText("Rooted Device!!");
        }
        else
        {
            root_status.setText("Device not Rooted!!");
        }
    }

    private boolean doesSUexist() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/bin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }

    }

    private boolean doesSuperuserApkExist(String s) {

        File rootFile = new File("/system/app/Superuser.apk");
        Boolean doesexist = rootFile.exists();
        if(doesexist == true)
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    /*
    The page that allows the user to allow password change for the logged in user
    */
	protected void changePasswd() {
		// TODO Auto-generated method stub
		Intent cP = new Intent(getApplicationContext(), ChangePassword.class);
		cP.putExtra("uname", uname);
		startActivity(cP);
	}

	/*
	The function that allows the user to view transaction history for the logged in user
	*/
	protected void viewStatment() {
		// TODO Auto-generated method stub
		Intent vS = new Intent(getApplicationContext(), ViewStatement.class);
		vS.putExtra("uname", uname);
		startActivity(vS);
	}
	// Added for handling menu operations
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Added for handling menu operations
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar wil
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			callPreferences();
			return true;
		} else if (id == R.id.action_exit) {
			Intent i = new Intent(getBaseContext(), LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void callPreferences() {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, FilePrefActivity.class);
		startActivity(i);
	}
}