package com.codepath.Weightroom.ui.login;

import android.os.Bundle;
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
    TextView tvUsername;
    TextView tvDescription;
    TextView relTime;
    LinearLayout tvMedia;
    public static final String TAG = "DetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content view only needs to be called once at the top
        setContentView(R.layout.activity_details);

        exercise = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        tvMedia = findViewById(R.id.tvMedia);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.exDescription);
        tvUsername = findViewById(R.id.exName);
        relTime = findViewById(R.id.relTime);
        tvDescription.setText(exercise.getDescription());
        tvUsername.setText(exercise.getUser().getUsername());
        relTime.setText(exercise.calculateTimeAgo(exercise.getCreatedAt()));
        ParseFile image = exercise.getImage();
        //load imageurl with Glide (included in build gradle already)
        Glide.with(this).load(image.getUrl()).into(ivImage);



    }

}
