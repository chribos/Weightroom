package com.codepath.simpleinsta.ui.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.codepath.simpleinsta.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity  extends AppCompatActivity {
    private Button logout;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 3421;
    private Button btnCapture;
    private EditText etDescription;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private String TAG = "MainActivity";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logout = findViewById(R.id.logout);
        btnCapture= findViewById(R.id.btnCapture);
        btnSubmit= findViewById(R.id.btnSubmit);
        btnCapture= findViewById(R.id.btnCapture);
        ivPostImage = findViewById(R.id.ivPostImg);
        etDescription = findViewById(R.id.etDescription);

        //this command retrieves a query of all posts in DB
         //queryPosts();

        //create listener for capture button which will allow pictures to be taken with camera using implicit intent (native app_
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        //create listener for submit button, collect all information and create post from it
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if(description.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "The description cannot be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //make sure photo file is not empty
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(MainActivity.this, "No image found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }

        });

        //create click listener for logout button
        logout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                //log user out
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                goLoginActivity();

            }
        });
    }

    //method for launching camera
    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        //checks for camwea app
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo

            //request code is just an arbitrary number that we define
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    //this will allow user to navigate back to app after taking picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            //checks if picture was actually taken
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);


    }

    //method for saving post to DB
    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        //saves post to database
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null) {
                    Log.e(TAG, "issue with saving posts", e);
                    Toast.makeText(MainActivity.this, "Error saving post", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "post saved!", e);
                Toast.makeText(MainActivity.this, "Post saved!", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                //empty image
                ivPostImage.setImageResource(0);

            }
        });

    }

    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        //include user information in query to get author of post
        query.include(Post.KEY_USER);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!= null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                //iterate through posts if successful and
                for (Post post: posts) {
                    Log.i(TAG, "post:" + post.getDescription()+ "user:"+ post.getUser().getUsername());
                }
            }
        });


    }
    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}

