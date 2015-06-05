package com.dreamdom.tutorials.shoppingcart;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	Button btnRegister, btnLinkToLogin;
	EditText inputFullName, inputEmail, inputPassword, inputPhone,
			inputAddress, inputPincode;
	HttpPost httppost;
	StringBuffer buffer;
	String img, username;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		inputFullName = (EditText) findViewById(R.id.name);
		inputPhone = (EditText) findViewById(R.id.phone);
		inputEmail = (EditText) findViewById(R.id.email);
		inputAddress = (EditText) findViewById(R.id.address);
		inputPincode = (EditText) findViewById(R.id.pincode);
		inputPassword = (EditText) findViewById(R.id.password);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// String name = inputFullName.getText().toString();
				// String phone = inputPhone.getText().toString();
				// String email = inputEmail.getText().toString();
				// String password = inputPassword.getText().toString();
				if (inputFullName.getText().toString().trim().length() == 0
						|| inputEmail.getText().toString().trim().length() == 0
						|| inputPhone.getText().toString().trim().length() == 0) {
					inputFullName.setError("Cannot be empty");
					inputEmail.setError("Cannot be empty");
					inputPhone.setError("cannot be empty");
					inputPassword.setError("Cannot be empty");
					inputAddress.setError("Cannot be empty");
					inputPincode.setError("Cannot be empty");
				} else if (!isValidEmail(inputEmail.getText().toString())) {
					inputEmail.setError("Invalid Email");
				}

				else if (inputPassword.getText().toString().trim().length() < 6)

				{
					inputPassword
							.setError("Password Must Have More than 6 characters");
				} else if (!isValidPhoneno(inputPhone.getText().toString())) {
					inputPhone.setError("Invalid Phone Number");
				}

				else {
					dialog = ProgressDialog.show(RegisterActivity.this, "",
							"Validating user...", true);
					new Thread(new Runnable() {
						public void run() {
							submit();
							login();

						}
					}).start();

				}
			}
		});
		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isValidPhoneno(String number) {
		String PHONE_REGEX = "[0-9]{10}";
		Pattern pattern = Pattern.compile(PHONE_REGEX);
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();

	}

	public void submit() {

		try {

			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(
					"http://192.168.245.1/supermarketphp/userregister.php"); // make
																				// sure
																				// the
																				// url
																				// is
																				// correct.
			// add your data
			nameValuePairs = new ArrayList<NameValuePair>(6);
			// Always use the same variable name for posting i.e the android
			// side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("username", inputFullName
					.getText().toString())); // $Edittext_value =
												// $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("phone", inputPhone
					.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("email", inputEmail
					.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("password", inputPassword
					.getText().toString().trim())); // $Edittext_value =
													// $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("address", inputAddress
					.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("pincode", inputPincode
					.getText().toString().trim()));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			// response=httpclient.execute(httppost);
			// edited by James from coderzheaven.. <span class="IL_AD"
			// id="IL_AD7">from here</span>....
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost,
					responseHandler);
			System.out.println("Response : " + response);
			runOnUiThread(new Runnable() {
				public void run() {
					// tvv.setText("Response from PHP : " + response);

					dialog.dismiss();

				}
			});

			if (response.equalsIgnoreCase("success")) {
				runOnUiThread(new Runnable() {
					public void run() {

						// Toast.makeText(getApplicationContext(),
						// "submitted",Toast.LENGTH_LONG).show();
						// Intent intent=new Intent(RegisterActivity.this,
						// MainActivity.class);
						// startActivity(intent);
					}
				});
			} else {
				// checkfields();
			}

		} catch (Exception e) {
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}

	}

	public void login() {

		try {

			httppost = new HttpPost(
					"http://192.168.245.1/supermarketphp/logindata.php"); // make
																			// sure
																			// the
																			// url
																			// is
																			// correct.
			// add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android
			// side variable name and php side variable name should be similar,

			nameValuePairs.add(new BasicNameValuePair("email", inputEmail
					.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("password", inputPassword
					.getText().toString().trim())); // $Edittext_value =
													// $_POST['Edittext_value'];
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			// response=httpclient.execute(httppost);
			// edited by James from coderzheaven.. <span class="IL_AD"
			// id="IL_AD7">from here</span>....
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost,
					responseHandler);
			System.out.println("Response : " + response);
			runOnUiThread(new Runnable() {
				public void run() {
					// tvv.setText("Response from PHP : " + response);

					dialog.dismiss();

				}
			});

			if (response.equalsIgnoreCase("success")) {
				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(getApplicationContext(), "submitted",
								Toast.LENGTH_LONG).show();
						Intent intent = new Intent(RegisterActivity.this,
								MainActivity.class);
						startActivity(intent);
					}
				});
			} else {
				// checkfields();
			}

		} catch (Exception e) {
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}
}
