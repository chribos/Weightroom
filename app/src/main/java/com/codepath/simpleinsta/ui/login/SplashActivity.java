package com.codepath.simpleinsta.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.codepath.simpleinsta.R;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
//        getActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent home = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(home);
                finish();

            }
        }, 3000);
    }
}