package com.codepath.simpleinsta.ui.login;

import android.app.Application;
import com.parse.Parse;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("yhyQU5yoN6duvOgP137HJpKFot9nmZydr4LyMIsW")
                .clientKey("jRaxSByE9dKWUfSDqhxDaKDGC6OFy64qrKkVeeIA")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
