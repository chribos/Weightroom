package com.codepath.Weightroom.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.Weightroom.R;
import com.parse.ParseFile;

import org.parceler.Parcels;


public class DetailsActivity extends AppCompatActivity {
    Exercise exercise;
    ImageView ivImage;
    TextView exTitle;
    TextView exDescription;
    TextView exEquipment;
    LinearLayout tvMedia;
    Workout workout;
    public static final String TAG = "DetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        workout = Parcels.unwrap(getIntent().getParcelableExtra("w"));
        exercise = Parcels.unwrap(getIntent().getParcelableExtra("e"));
        if (exercise != null) {
            Log.d(TAG, String.format("Showing details for %s", exercise.getExDescription()));

            tvMedia = findViewById(R.id.tvMedia);
            exTitle = findViewById(R.id.exTitle);
            exDescription = findViewById(R.id.exDescription);
            exEquipment = findViewById(R.id.exEquipment);

            exTitle.setText(exercise.getExTitle());
            Log.i(TAG, "problem" + exercise.getExTitle());
            exDescription.setText(exercise.getExDescription());
            exEquipment.setText("Equipment: " + exercise.getExEquipment());
        } else {
            Log.d(TAG, String.format("Showing details for %s", workout.getDescription()));

            tvMedia = findViewById(R.id.tvMedia);
            exTitle = findViewById(R.id.exTitle);
            exDescription = findViewById(R.id.exDescription);
            exEquipment = findViewById(R.id.exEquipment);

            exTitle.setText(workout.getTitle());
            Log.i(TAG, "problem" + workout.getTitle());
            exDescription.setText(workout.getDescription());
            exEquipment.setText("Equipment: " + workout.getEquipment());
        }
        getSupportActionBar().hide();

//        exEquipment.setText(exercise.getExEquipment());
//        relTime.setText(exercise.calculateTimeAgo(exercise.getCreatedAt()));
//        ParseFile image = exercise.getImage();
        //load imageurl with Glide (included in build gradle already)
//        Glide.with(this).load(image.getUrl()).into(ivImage);



    }

}
