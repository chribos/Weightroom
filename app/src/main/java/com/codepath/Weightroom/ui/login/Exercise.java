package com.codepath.Weightroom.ui.login;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

//@ParseClassName("Exercise")
@Parcel(analyze = {Exercise.class})
public class Exercise {

//    public static final String KEY_DESCRIPTION = "description";
//    public static final String KEY_IMAGE = "image";
//    public static final String KEY_USER = "user";
    String exTitle;
    String exDescription;
    ArrayList<String> equip;

    public Exercise() {}
    public Exercise(JSONObject exercise) throws JSONException {
        exTitle = exercise.getString("name");
        exDescription = exercise.getString("description");
        equip = new ArrayList<String>();
        JSONArray equipArray = exercise.getJSONArray("equipment");
        if (equipArray.length()> 0 && (exercise.getJSONArray("equipment").getJSONObject(0).getInt("id")) != 7) {
            for (int i = 0; i < equipArray.length(); i++) {
                equip.add(exercise.getJSONArray("equipment").getJSONObject(i).getString("name"));
            }
        }
        else {
            equip.add("Bodyweight exercise");
        }
    }

    //turn array into list of movies
    public static List<Exercise> fromJsonArray(JSONArray exerciseJsonArray) throws JSONException {
        List<Exercise> exercises = new ArrayList<>();
        for (int i =0; i<exerciseJsonArray.length(); i++) {
            //add exercise at each position of array
            exercises.add(new Exercise(exerciseJsonArray.getJSONObject(i)));
        }
        return exercises;
    }
    public String getExEquipment() {
        StringBuilder sbString = new StringBuilder("");

        //iterate through ArrayList
        for(String equipment : equip){

            //append ArrayList element followed by comma
            sbString.append(equipment).append(",");
        }

        //convert StringBuffer to String
        String strList = sbString.toString();

        //remove last comma from String if you want
        if( strList.length() > 0 )
            strList = strList.substring(0, strList.length() - 1);
        return strList;
    }

    public String getExTitle() {
        return exTitle;
    }

//    public String getDescription() {
//        return getString(KEY_DESCRIPTION);
//    }

//    public String getExDescription() {
//        return exDescription;
//    }


    public String getExDescription() {
        return exDescription;
    }


//    public void setDescription(String description) {
//        put(KEY_DESCRIPTION, description);
//    }

//    public ParseFile getImage () {
//        return getParseFile(KEY_IMAGE);
//    }
//
//    public void setImage(ParseFile parseFile) {
//        put(KEY_IMAGE, parseFile);
//    }
//
//    public ParseUser getUser () {
//        return getParseUser(KEY_USER);
//    }
//
//    public void setUser(ParseUser user) {
//        put(KEY_USER, user);
//    }
//    public static String calculateTimeAgo(Date createdAt) {
//
//        int SECOND_MILLIS = 1000;
//        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
//        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
//        int DAY_MILLIS = 24 * HOUR_MILLIS;
//
//        try {
//            createdAt.getTime();
//            long time = createdAt.getTime();
//            long now = System.currentTimeMillis();
//
//            final long diff = now - time;
//            if (diff < MINUTE_MILLIS) {
//                return "just now";
//            } else if (diff < 2 * MINUTE_MILLIS) {
//                return "a minute ago";
//            } else if (diff < 50 * MINUTE_MILLIS) {
//                return diff / MINUTE_MILLIS + " m";
//            } else if (diff < 90 * MINUTE_MILLIS) {
//                return "an hour ago";
//            } else if (diff < 24 * HOUR_MILLIS) {
//                return diff / HOUR_MILLIS + " h";
//            } else if (diff < 48 * HOUR_MILLIS) {
//                return "yesterday";
//            } else {
//                return diff / DAY_MILLIS + " d";
//            }
//        } catch (Exception e) {
//            Log.i("Error:", "getRelativeTimeAgo failed", e);
//            e.printStackTrace();
//        }
//
//        return "";
//    }
}
