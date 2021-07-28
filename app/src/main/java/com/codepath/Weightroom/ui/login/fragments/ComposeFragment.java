package com.codepath.Weightroom.ui.login.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.Weightroom.R;
import com.codepath.Weightroom.ui.login.LoginActivity;
import com.codepath.Weightroom.ui.login.Exercise;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {
    private Button logout;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 3421;
    private Button btnCapture;
    private EditText etDescription;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    public String TAG = "ComposeFragment";
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ComposeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ComposeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ComposeFragment newInstance(String param1, String param2) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    //all view setups for fragments should be done on onViewCreated rather than onCreateView
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logout = view.findViewById(R.id.logout);
        btnCapture= view.findViewById(R.id.btnCapture);
        btnSubmit= view.findViewById(R.id.btnSubmit);
        btnCapture= view.findViewById(R.id.btnCapture);
        ivPostImage = view.findViewById(R.id.ivPostImg);
        etDescription = view.findViewById(R.id.etDescription);

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
                    //use getContext() instead of classnames.this (MainActivity.this for example)
                    Toast.makeText(getContext(),
                            "The description cannot be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //make sure photo file is not empty
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "No image found!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
//                savePost(description, currentUser, photoFile);
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
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        //checks for camera app
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo

            //request code is just an arbitrary number that we define
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    //this will allow user to navigate back to app after taking picture
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
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
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        /**Get safe storage directory for photos
         Use `getExternalFilesDir` on Context to access package-specific directories.
         This way, we don't need to request external read/write runtime permissions. */
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);


    }

    //method for saving post to DB
//    private void savePost(String description, ParseUser currentUser, File photoFile) {
//        Exercise exercise = new Exercise();
//        exercise.setDescription(description);
//        exercise.setImage(new ParseFile(photoFile));
//        exercise.setUser(currentUser);
//        //saves post to database
//        exercise.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e!= null) {
//                    Log.e(TAG, "issue with saving posts", e);
//                    Toast.makeText(getContext(), "Error saving post", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Log.i(TAG, "post saved!", e);
//                Toast.makeText(getContext(), "Post saved!", Toast.LENGTH_SHORT).show();
//                etDescription.setText("");
//                //empty image
//                ivPostImage.setImageResource(0);
//
//            }
//        });
//
//    }
    //this command retrieves a query of all posts in DB
//    private void queryPosts() {
//        ParseQuery<Exercise> query = ParseQuery.getQuery(Exercise.class);
//
//        //include user information in query to get author of post
////        query.include(Exercise.KEY_USER);
//
//        query.findInBackground(new FindCallback<Exercise>() {
//            @Override
//            public void done(List<Exercise> exercises, ParseException e) {
//                if (e!= null) {
//                    Log.e(TAG, "issue with getting posts", e);
//                    return;
//                }
//                //iterate through posts if successful and
//                for (Exercise exercise : exercises) {
////                    Log.i(TAG, "post:" + exercise.getDescription()+ "user:"+ exercise.getUser().getUsername());
//                }
//            }
//        });
//
//
//    }
    private void goLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
    }
}