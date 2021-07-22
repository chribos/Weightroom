package com.codepath.Weightroom.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.Weightroom.R;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;


public class PromptActivity extends AppCompatActivity {
    List<String> equipmentList;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    EquipmentAdapter EquipmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();
        /*add arbitrary hardcoded items to test the model display
        items = new ArrayList<>();
        items.add("Buy goat cheese");
        items.add("Go skiing");
        items.add("lift");*/

        //ctrl+o to see override methods (selected onitemlongclicked in this case)
        EquipmentAdapter.OnLongClickListener onLongClickListener =  new EquipmentAdapter.OnLongClickListener() {

            @Override
            public void onItemLongClicked(int position) {
                //delete item from model
                equipmentList.remove(position);
                //make itemsAdapter field (under rv at the top to notify the adapter of an item removed)
                EquipmentAdapter.notifyItemRemoved(position);
                //create 'toast' notifying user that their item has been removed
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        //construct adapter here now and add second parameter for long click once finished
        EquipmentAdapter = new EquipmentAdapter(equipmentList, onLongClickListener);
        rvItems.setAdapter(EquipmentAdapter);
        //displays items vertically on UI
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        //create on-click listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get contents of user input text as soon as add is clicked
                String equipItem = etItem.getText().toString();
                //we have to also add item to model (string list of items which is placed on rv)
                equipmentList.add(equipItem);

                //we have to also notify the adapter that an item has been added to last position
                EquipmentAdapter.notifyItemInserted(equipmentList.size()-1);

                //now we clear edit text once inserted
                etItem.setText("");
                //create 'toast' notifying user that their item has been added
                Toast.makeText(getApplicationContext(), "Equipment added", Toast.LENGTH_SHORT).show();
                //save items to datafile
                saveItems();
            }
        });
    }
    //add commons library in build.gradle and this returns the file in which we store the items for persistence
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    //this loads items by reading individual file lines
    private void loadItems() {
        try {
            //read lines from data file and populate that into an arraylist
            equipmentList = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("PromptActivity", "Error reading items", e);
            equipmentList = new ArrayList<>();
        }
    }
    //writes items into data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), equipmentList);
        } catch (IOException e) {
            Log.e("PromptActivity", "Error writing equipment", e);
        }

    }

}