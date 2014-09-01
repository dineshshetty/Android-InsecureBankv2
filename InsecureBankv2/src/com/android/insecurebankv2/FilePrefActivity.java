package com.android.insecurebankv2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
The page that allows user to set the server IP address and port number
to which the InsecureBank application has to connect to
@author Dinesh Shetty
*/

public class FilePrefActivity extends Activity {
	//	The EditText that stores the user entered server IP address
	static EditText edit_serverip;
	//	The EditText that stores the user entered server port number
	static EditText edit_serverport;
	//  The Button that handles the save preference activity
	Button submitPref_buttonz;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_pref);
		submitPref_buttonz = (Button) findViewById(R.id.submitPref_button);
		edit_serverip = (EditText) findViewById(R.id.edittext_serverip);
		edit_serverport = (EditText) findViewById(R.id.edittext_serverport);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		submitPref_buttonz.setOnClickListener(new View.OnClickListener() {@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//  Save the user entered IP address and port number in a local shared preference file
				setPreferences();
			}
		});
	}
	// Added for menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Added for menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	/*
	The function that saves the user entered server IP address
	and port number locally for future reference
	*/
	protected void setPreferences() {
		// TODO Auto-generated method stub
		editor.putString("serverip", edit_serverip.getText().toString());
		editor.putString("serverport", edit_serverport.getText().toString());
		editor.commit();
		finish();
	}

}