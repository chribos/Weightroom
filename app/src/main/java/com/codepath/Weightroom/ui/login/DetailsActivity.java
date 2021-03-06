package com.codepath.Weightroom.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.Weightroom.R;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.parse.ParseFile;

import org.parceler.Parcels;


public class DetailsActivity extends AppCompatActivity {
    Exercise exercise;
    TextView exTitle;
    TextView exDescription;
    TextView exEquipment;
    LinearLayout tvMedia;
    Workout workout;
    TextView exPrimary;
    TextView exSecondary;
    ImageView ivPrimary;
    ImageView ivSecondary;

    public static final String TAG = "DetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvMedia = findViewById(R.id.tvMedia);
        exTitle = findViewById(R.id.exTitle);
        exDescription = findViewById(R.id.exDescription);
        exEquipment = findViewById(R.id.exEquipment);
        exPrimary = findViewById(R.id.exPrimary);
        exSecondary = findViewById(R.id.exSecondary);
        ivPrimary = (ImageView) findViewById(R.id.ivPrimary);
        ivSecondary = (ImageView) findViewById(R.id.ivSecondary);

        workout = Parcels.unwrap(getIntent().getParcelableExtra("w"));
        exercise = Parcels.unwrap(getIntent().getParcelableExtra("e"));
        if (exercise != null) {
            Log.d(TAG, String.format("Showing details for %s", exercise.getExDescription()));


            exTitle.setText(exercise.getExTitle());
            Log.i(TAG, "problem" + exercise.getExTitle());
            exDescription.setText(exercise.getExDescription());
            exEquipment.setText("Equipment: " + exercise.getExEquipment());
            exPrimary.setText(exercise.getExPrimary());
            exSecondary.setText(exercise.getExSecondary());

            Log.i("PrimaryURL", exercise.getExPrimaryPath());
            Log.i("SecondaryURL", exercise.getExSecondaryPath());

            //third-party library for .svg functionality for Glide.
            GlideToVectorYou.justLoadImage(DetailsActivity.this, Uri.parse(exercise.getExPrimaryPath()), ivPrimary);
            GlideToVectorYou.justLoadImage(DetailsActivity.this, Uri.parse(exercise.getExSecondaryPath()), ivSecondary);

;

        } else {
            Log.d(TAG, String.format("Showing details for %s", workout.getDescription()));

            exTitle.setText(workout.getTitle());
            Log.i(TAG, "problem" + workout.getTitle());
            exDescription.setText(workout.getDescription());
            exEquipment.setText("Equipment: " + workout.getEquipment());
            exPrimary.setText(workout.getPrimary());
            exSecondary.setText(workout.getSecondary());
            GlideToVectorYou.justLoadImage(DetailsActivity.this, Uri.parse(workout.getPrimaryUrl()), ivPrimary);
            GlideToVectorYou.justLoadImage(DetailsActivity.this, Uri.parse(workout.getSecondaryUrl()), ivSecondary);


        }
        getSupportActionBar().hide();

//        exEquipment.setText(exercise.getExEquipment());
//        relTime.setText(exercise.calculateTimeAgo(exercise.getCreatedAt()));
//        ParseFile image = exercise.getImage();
        //load imageurl with Glide (included in build gradle already)
//        Glide.with(this).load(image.getUrl()).into(ivImage);



    }

}
