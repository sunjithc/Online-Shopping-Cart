package com.dreamdom.tutorials.shoppingcart;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;





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

public class MainActivity extends Activity {
EditText inputEmail,inputPassword;
Button btnLogin,btnLinkToRegister;
ProgressDialog dialog = null;
HttpPost httppost;
StringBuffer buffer;
HttpResponse response;
List<NameValuePair> nameValuePairs;
HttpClient httpclient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();

				// Check for empty data in the form
				if((inputEmail.getText().toString().trim().length() == 0 ||
            			inputPassword.getText().toString().trim().length() == 0 )){
                      inputEmail.setError("Cannot be empty");
                      inputPassword.setError("Cannot be empty");
                      
                }else{
                dialog = ProgressDialog.show(MainActivity.this, "", 
                        "Validating user...", true);
                 new Thread(new Runnable() {
                        public void run() {
                            login();                          
                        }
                      }).start();               
            }}

		});
		
		// Link to Register Screen
				btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

					public void onClick(View view) {
						Intent i = new Intent(getApplicationContext(),
								RegisterActivity.class);
						startActivity(i);
						finish();
					}
				});

	}
	 void login(){
	        try{            
	              
	            httpclient=new DefaultHttpClient();
	            httppost= new HttpPost("http://192.168.245.1/supermarketphp/login.php"); // make sure the url is correct.
	            //add your data
	            nameValuePairs = new ArrayList<NameValuePair>(2);
	            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
	            nameValuePairs.add(new BasicNameValuePair("username",inputEmail.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
	            nameValuePairs.add(new BasicNameValuePair("password",inputPassword.getText().toString().trim())); 
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            //Execute HTTP Post Request
	            response=httpclient.execute(httppost);
	            // edited by James from coderzheaven.. from here....
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            final String response = httpclient.execute(httppost, responseHandler);
	            System.out.println("Response : " + response); 
	            runOnUiThread(new Runnable() {
	                public void run() {
	                   
	                    dialog.dismiss();
	                }
	            });
	             
	            if(response.equalsIgnoreCase("User Found")){
	                runOnUiThread(new Runnable() {
	                    public void run() {
	                        Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
	                        Intent intent=new Intent(MainActivity.this, CatalogActivity.class);
	    	                intent.putExtra("uname",inputEmail.getText().toString());
	    	                intent.putExtra("pass",inputPassword.getText().toString());
	    	                startActivity(intent);
	                    
	                    }
	                });
	               
	            }else{
	                showAlert();                
	            }
	             
	        }catch(Exception e){
	            dialog.dismiss();
	            System.out.println("Exception : " + e.getMessage());
	        }
	    }    
	    
	    
	    public void showAlert(){
	        MainActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
	                builder.setTitle("Login Error.");
	                builder.setMessage("User not Found.")  
	                       .setCancelable(false)
	                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                           public void onClick(DialogInterface dialog, int id) {
	                           }
	                       });                     
	                AlertDialog alert = builder.create();
	                alert.show();               
	            }
	        });
	    }
}
