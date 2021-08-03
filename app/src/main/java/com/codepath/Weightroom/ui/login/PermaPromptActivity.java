package com.codepath.Weightroom.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.Weightroom.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

//this class is one that makes sure the prompt is displayed regardless if the user has seen it before
public class PermaPromptActivity extends AppCompatActivity {
    ArrayList<String> equipmentList;

    public final String TAG = "promptActivity";

    Button btnAdd,  selectAll, deselectAll;
    CheckBox cb1, cb2, cb3, cb4, cb5;
    ImageView selectImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        getSupportActionBar().hide();

        btnAdd = findViewById(R.id.btnAdd);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);

        selectImg = findViewById(R.id.selectImg);

        equipmentList = new ArrayList<String>();

        selectAll= findViewById(R.id.selectAll);
        selectAll.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (!cb1.isChecked()) {cb1.setChecked(true);}
                if (!cb2.isChecked()) {cb2.setChecked(true);}
                if (!cb3.isChecked()) {cb3.setChecked(true);}
                if (!cb4.isChecked()) {cb4.setChecked(true);}
                if (!cb5.isChecked()) {cb5.setChecked(true);}
            }
        });
        deselectAll= findViewById(R.id.deselectAll);
        deselectAll.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (cb1.isChecked()) {cb1.setChecked(false);}
                if (cb2.isChecked()) {cb2.setChecked(false);}
                if (cb3.isChecked()) {cb3.setChecked(false);}
                if (cb4.isChecked()) {cb4.setChecked(false);}
                if (cb5.isChecked()) {cb5.setChecked(false);}
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb1.isChecked()) {equipmentList.add(cb1.getText().toString());}
                if (cb2.isChecked()) {equipmentList.add(cb2.getText().toString());}
                if (cb3.isChecked()) {equipmentList.add(cb3.getText().toString());}
                if (cb4.isChecked()) {equipmentList.add(cb4.getText().toString());}
                if (cb5.isChecked()) {equipmentList.add(cb5.getText().toString());}

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(equipmentList, currentUser);

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();

                Log.i(TAG, equipmentList.toString());

            }
        });
    }
    private void savePost(ArrayList equipmentList, ParseUser currentUser) {
        Equipment post = new Equipment();
        post.setEquipment(equipmentList);
        post.setUser(currentUser);

        //saves post to database
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null) {
                    Log.e(TAG, "issue with saving posts", e);
                    Toast.makeText(getBaseContext(), "Error saving post", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "post saved!", e);
                Toast.makeText(getBaseContext(), "Post saved!", Toast.LENGTH_SHORT).show();



            }
        });

    }

}
