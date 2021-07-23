package com.codepath.Weightroom.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.Weightroom.R;

import java.util.List;


public class PromptActivity extends AppCompatActivity {
    List<String> equipmentList;

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

        //construct adapter here now and add second parameter for long click once finished


        //create on-click listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get contents of user input text as soon as add is clicked
                String equipItem = selectAll.getText().toString();
                //we have to also add item to model (string list of items which is placed on rv)
                equipmentList.add(equipItem);

                //we have to also notify the adapter that an item has been added to last position
//                EquipmentAdapter.notifyItemInserted(equipmentList.size()-1);

                //now we clear edit text once inserted

                //create 'toast' notifying user that their item has been added
                Toast.makeText(getApplicationContext(), "Equipment added", Toast.LENGTH_SHORT).show();
                //save items to datafile

            }
        });
    }


}