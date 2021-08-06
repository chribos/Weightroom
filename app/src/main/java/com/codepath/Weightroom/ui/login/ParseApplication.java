package com.codepath.Weightroom.ui.login;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //register past module "Post" that we just created
        ParseObject.registerSubclass(Workout.class);
        ParseObject.registerSubclass(Equipment.class);
        ParseObject.registerSubclass(User.class);
//        ParseObject.registerSubclass(Workout.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("U7O0jWNcE4YAnoYo2K8mHDbiwFAoFngGDzUeuxFE")
                .clientKey("FkpnaKOXa1FEi2rCYV6HnNycYKjPI76HYf5651I7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
