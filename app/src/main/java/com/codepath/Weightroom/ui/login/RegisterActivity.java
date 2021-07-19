package com.codepath.Weightroom.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.Weightroom.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
//import com.parse.ParseException;
//import com.parse.ParseUser;
//import com.parse.SignUpCallback;

import org.parceler.Parcels;

public class RegisterActivity extends AppCompatActivity {
    ImageView imageView2;
    TextView username2;
    TextView password2;
    TextView email;
    Button register2;
    String newUser;
    String newEmail;
    String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content view only needs to be called once at the top
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        imageView2 = findViewById(R.id.imageView2);
        username2 = findViewById(R.id.username2);
        password2 = findViewById(R.id.password2);
        email = findViewById(R.id.email);
        register2 = findViewById(R.id.register2);


        newUser = username2.getText().toString();
        newEmail = email.getText().toString();
        newPassword = password2.getText().toString();
        if (newUser != null && newEmail != null && newPassword != null) {
            register2.setEnabled(true);
            register2.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick(View v) {
                    // Create the ParseUser
                    Log.e("Username:", newUser);
                    ParseUser user = new ParseUser();
                    // Set core properties
                    user.setUsername( username2.getText().toString());
                    user.setPassword(password2.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Toast.makeText(getApplicationContext(),
                                        "Account created!", Toast.LENGTH_SHORT).show();
                                Log.i("Register", "account success");
                                goLoginActivity();
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                Log.e("Register", "account fail",e );
                                Toast.makeText(getApplicationContext(),
                                        "Email and username must be unique", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            });
        }
        else {Toast.makeText(getApplicationContext(),
                "Email, username, and/or password cannot be null", Toast.LENGTH_SHORT).show();}
    }
        private void goLoginActivity () {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

