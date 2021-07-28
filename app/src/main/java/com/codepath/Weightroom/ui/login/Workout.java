package com.codepath.Weightroom.ui.login;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Workout")
public class Workout extends ParseObject {
    public static final String KEY_DESCRIPTION = "exDescription";
    public static final String KEY_TITLE = "exTitle";
    public static final String KEY_CATEGORY = "exCategory";
    public static final String KEY_EQUIPMENT = "exEquipment";
    public static final String KEY_USER = "user";

    public ParseUser getUser () {
        return getParseUser(KEY_USER);
    }
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public String getTitle() {
        return getString(KEY_TITLE);
    }
    public String getCategory() {
        return getString(KEY_CATEGORY);
    }
    public String getEquipment() {
        return getString(KEY_EQUIPMENT);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }



}