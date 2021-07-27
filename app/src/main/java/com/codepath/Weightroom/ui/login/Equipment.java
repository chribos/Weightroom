package com.codepath.Weightroom.ui.login;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Equipment")
public class Equipment extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_EQUIPMENT = "equipment";
    public static final String KEY_USER = "user";


    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseUser getUser () {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public List getEquipment() {
        return getList(KEY_EQUIPMENT);
    }


    public void setEquipment(ArrayList equipment) {
        put(KEY_EQUIPMENT, equipment);
    }

}