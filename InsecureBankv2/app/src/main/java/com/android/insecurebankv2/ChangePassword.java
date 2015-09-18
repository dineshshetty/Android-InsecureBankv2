package com.android.insecurebankv2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*
The page that accepts new password and passes it on to the change password 
module. This new password can then be used by the user to log in to the account.
@author Dinesh Shetty
*/
public class ChangePassword extends Activity {
	//	The EditText that holds the new password entered by the user
	EditText changePassword_text;
	//	The TextView that automatically grabs the current logged in user's username
	TextView textView_Username;
	// The Button that maps to the change password-Submit button 
	Button changePassword_button;
	//	Regex to ensure password is complex enough
    private static final String PASSWORD_PATTERN = 
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	private Pattern pattern;
    private Matcher matcher;
	String uname;
	String result;
	BufferedReader reader;
	String serverip = "";
	String serverport = "";
	String protocol = "http://";
	SharedPreferences serverDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);

        // Get Server details from Shared Preference file.
		serverDetails = PreferenceManager.getDefaultSharedPreferences(this);
		serverip = serverDetails.getString("serverip", null);
		serverport = serverDetails.getString("serverport", null);

		changePassword_text = (EditText) findViewById(R.id.editText_newPassword);
		Intent intent = getIntent();
		uname = intent.getStringExtra("uname");
		System.out.println("newpassword=" + uname);
		textView_Username = (TextView) findViewById(R.id.textView_Username);
		textView_Username.setText(uname);

        // Manage the change password button click
		changePassword_button = (Button) findViewById(R.id.button_newPasswordSubmit);
		changePassword_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new RequestChangePasswordTask().execute(uname);
			}
		});
	}
	class RequestChangePasswordTask extends AsyncTask < String, String, String > {

		@Override
		protected String doInBackground(String...params) {

			try {
				postData(params[0]);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Double result) {

		}
		protected void onProgressUpdate(Integer...progress) {

		}

		/*
		The function that makes an HTTP Post to the server endpoint that handles the 
		change password operation.
		*/
		public void postData(String valueIWantToSend) throws ClientProtocolException, IOException, JSONException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(protocol + serverip + ":" + serverport + "/changepassword");
			List < NameValuePair > nameValuePairs = new ArrayList < NameValuePair > (2);

			/*
			   Delete below test accounts once the application goes into production phase.
			   nameValuePairs.add(new BasicNameValuePair("username", "jack"));
			   nameValuePairs.add(new BasicNameValuePair("password", "Jack@123$"));
			 */
			nameValuePairs.add(new BasicNameValuePair("username", uname));
			nameValuePairs.add(new BasicNameValuePair("newpassword", changePassword_text.getText().toString()));
			HttpResponse responseBody;
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			pattern = Pattern.compile(PASSWORD_PATTERN);
			matcher = pattern.matcher(changePassword_text.getText().toString());

			// Check if the password is complex enough
			boolean isStrong= matcher.matches();
			if (isStrong){
				responseBody = httpclient.execute(httppost);
				InputStream in = responseBody.getEntity().getContent();
				result = convertStreamToString( in );
				result = result.replace("\n", "");

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (result != null) {
							if (result.indexOf("Change Password Successful") != -1) {
								//	Below code handles the Json response parsing 
								JSONObject jsonObject;
								try {
									jsonObject = new JSONObject(result);
									String login_response_message = jsonObject.getString("message");
									Toast.makeText(getApplicationContext(), login_response_message + ". Restart application to Continue.", Toast.LENGTH_LONG).show();
                                    TelephonyManager phoneManager = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                                    String phoneNumber = phoneManager.getLine1Number();
                                    System.out.println("phonno:"+phoneNumber);

                                    /*
                                    The function that handles the SMS activity
                                    phoneNumber: Phone number to which the confirmation SMS is to be sent
                                    */

                                    broadcastChangepasswordSMS(phoneNumber, changePassword_text.getText().toString());


                                } catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				});
			}
			else
			{
				runOnUiThread(new Runnable() {
			    @Override
				public void run() {
				Toast.makeText(getApplicationContext(), "Entered password is not complex enough.", Toast.LENGTH_LONG).show();
				}
				});
			}
		}


		private String convertStreamToString(InputStream in ) throws IOException {
			// TODO Auto-generated method stub
			try {
				reader = new BufferedReader(new InputStreamReader( in , "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			} in .close();
			return sb.toString();
		}

	}

    private void broadcastChangepasswordSMS(String phoneNumber, String pass) {

        if(TextUtils.isEmpty(phoneNumber.toString().trim())) {

            System.out.println("Phone number Invalid.");
        }
        else
        {
            Intent smsIntent = new Intent();
            smsIntent.setAction("theBroadcast");
         //   String actdns= smsIntent.getAction().toString();
          //  Toast.makeText(getApplicationContext(),actdns , Toast.LENGTH_LONG).show();
            smsIntent.putExtra("phonenumber", phoneNumber);
            smsIntent.putExtra("newpass", pass);
            sendBroadcast(smsIntent);
        }

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