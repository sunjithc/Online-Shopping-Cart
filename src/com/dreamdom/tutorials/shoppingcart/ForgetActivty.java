package com.dreamdom.tutorials.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgetActivty extends Activity {
Button login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_activity);
		login=(Button) findViewById(R.id.forget_button);
		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent i =new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				
			}
		});
	}

}
