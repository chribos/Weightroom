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

    String exTitle;
    String exDescription;
    String exCategory;
    String exPrimary;
    String exSecondary;
    ArrayList<String> equip;

    public Exercise() {}
    public Exercise(JSONObject exercise) throws JSONException {
        exTitle = exercise.getString("name");
        exDescription = exercise.getString("description");
        exCategory = exercise.getJSONObject("category").getString("name");

        //primary muscle call
        if(exercise.getJSONArray("muscles").length()>0) {
            exPrimary = "Primary muscle worked: " + exercise.getJSONArray("muscles").getJSONObject(0).getString("name") +"\n";
        } else {exPrimary = "";}

        //secondary muscle call
        if(exercise.getJSONArray("muscles_secondary").length()>0) {
            exSecondary = "Secondary muscle worked:" + exercise.getJSONArray("muscles_secondary").getJSONObject(0).getString("name");
        } else {exSecondary = "";}


        equip = new ArrayList<String>();
        JSONArray equipArray = exercise.getJSONArray("equipment");
        if (equipArray.length()> 0 &&
                (exercise.getJSONArray("equipment").getJSONObject(0).getInt("id")) != 7) {
            for (int i = 0; i < equipArray.length(); i++) {
                equip.add(exercise.getJSONArray("equipment").getJSONObject(i).getString("name"));
            }
        }
        else {
            equip.add("Bodyweight exercise");
        }
    }

    //turn array into list of exercises
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


    public String getExPrimary() {
        return exPrimary;
    }
    public String getExSecondary() {
        return exSecondary;
    }

    public String getExDescription() {
        //removes tags like <p> </p> and adds a new line for the start of each sentence for polish
        exDescription = exDescription.replaceAll("\\<.*?\\>", "").replaceAll("\\.\\s?","\\.\n");
        return exDescription;
    }

    public String getExCategory() {
        return exCategory;
    }


}
