package com.codepath.simpleinsta.ui.login;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.simpleinsta.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    Post post;
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

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        tvMedia = findViewById(R.id.tvMedia);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        relTime = findViewById(R.id.relTime);
        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        relTime.setText(post.calculateTimeAgo(post.getCreatedAt()));
        ParseFile image = post.getImage();
        //load imageurl with Glide (included in build gradle already)
        Glide.with(this).load(image.getUrl()).into(ivImage);



    }

}
