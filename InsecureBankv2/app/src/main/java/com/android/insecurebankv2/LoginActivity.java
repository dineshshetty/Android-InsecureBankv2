package com.android.insecurebankv2;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.insecurebankv2.DoLogin;
import com.marcohc.toasteroid.Toasteroid;

/*
The page that accepts username and the password from the user. The credentials 
are then sent to the server and the user is allowed to proceed to the postlogin
pages on a successful authentication
@author Dinesh Shetty
*/
public class LoginActivity extends Activity {
	//	The Button that calls the authentication function
	Button login_buttons;
    //	The Button that calls the create user function
    Button createuser_buttons;
    //	The EditText that holds the username entered by the user
	EditText Username_Text;
	//	The EditText that holds the password entered by the user
	EditText Password_Text;
	//	The Button that allows the user to autofill the credentials, 
	//  if the user has logged in successfully earlier
	Button fillData_button;
	String usernameBase64ByteString;
	public static final String MYPREFS = "mySharedPreferences";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_main);
		String mess = getResources().getString(R.string.is_admin);
		if (mess.equals("no")) {
			View button_CreateUser = findViewById(R.id.button_CreateUser);
			button_CreateUser.setVisibility(View.GONE);
		}
		login_buttons = (Button) findViewById(R.id.login_button);
		login_buttons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                performlogin();
            }
        });
        createuser_buttons = (Button) findViewById(R.id.button_CreateUser);
        createuser_buttons.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                createUser();
            }
        });

        try {
            fillData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
//		fillData_button = (Button) findViewById(R.id.fill_data);
//		fillData_button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                try {
//                    fillData();
//                } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        });

	}

    /*
    The function that allows the user to create new user credentials.
    This functionality is available only to the admin user.
    <<WIP Code>>
    ToDo: Add functionality here.
    */
    protected void createUser() {
        Toasteroid.show(this, "Create User functionality is still Work-In-Progress!!", Toasteroid.STYLES.WARNING, Toasteroid.LENGTH_LONG);

    }

    /*
    The function that allows the user to autofill the credentials
    if the user has logged in successfully atleast one earlier using
    that device
    */
	protected void fillData() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences(MYPREFS, 0);
		final String username = settings.getString("EncryptedUsername", null);
        final String password = settings.getString("superSecurePassword", null);


        if(username!=null && password!=null)
        {
            byte[] usernameBase64Byte = Base64.decode(username, Base64.DEFAULT);
            try {
                usernameBase64ByteString = new String(usernameBase64Byte, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Username_Text = (EditText) findViewById(R.id.loginscreen_username);
            Password_Text = (EditText) findViewById(R.id.loginscreen_password);
            Username_Text.setText(usernameBase64ByteString);
            CryptoClass crypt = new CryptoClass();
            String decryptedPassword = crypt.aesDeccryptedString(password);
            Password_Text.setText(decryptedPassword);
        }
        else if (username==null || password==null)
        {
          //  Toast.makeText(this, "No stored credentials found!!", Toast.LENGTH_LONG).show();
        }
        else
        {
          //  Toast.makeText(this, "No stored credentials found!!", Toast.LENGTH_LONG).show();
        }

	}

	/*
	The function that passes the control on to the authentication module
	Username_Text: Username entered by the user
	Password_Text: password entered by the user
	*/
	protected void performlogin() {
		// TODO Auto-generated method stub
		Username_Text = (EditText) findViewById(R.id.loginscreen_username);
		Password_Text = (EditText) findViewById(R.id.loginscreen_password);
		Intent i = new Intent(this, DoLogin.class);
		i.putExtra("passed_username", Username_Text.getText().toString());
		i.putExtra("passed_password", Password_Text.getText().toString());
		startActivity(i);
	}

	// Added for handling menu operations
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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