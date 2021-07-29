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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.Weightroom.R;
import com.codepath.Weightroom.ui.login.CurrentAdapter;
import com.codepath.Weightroom.ui.login.Equipment;
import com.codepath.Weightroom.ui.login.ExercisesAdapter;
import com.codepath.Weightroom.ui.login.LoginActivity;
import com.codepath.Weightroom.ui.login.Exercise;
import com.codepath.Weightroom.ui.login.Workout;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComposeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeFragment extends Fragment {
    protected List<Workout> allWorkouts;
    public RecyclerView exCurrent;
    public Button exLogout;
    private SwipeRefreshLayout swipeContainer;


    private String TAG = "ComposeFragment";
    protected CurrentAdapter CurrentAdapter;
    protected ImageView homeIcon;

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
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeIcon = view.findViewById(R.id.homeIcon);
        exCurrent = view.findViewById(R.id.exCurrent);
        exLogout = view.findViewById(R.id.exLogout);
        swipeContainer = view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                CurrentAdapter.clear();
                queryWorkouts();
                swipeContainer.setRefreshing(false);
            }
        });


        // initialize the array that will hold exercises and create a PostsAdapter
        allWorkouts = new ArrayList<>();
        CurrentAdapter = new CurrentAdapter(getContext(), allWorkouts);


        // set the adapter on the recycler view of exercises that contain user's equipment
        exCurrent.setAdapter(CurrentAdapter);

        exLogout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                //log user out
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

            }
        });

        // set the layout manager on the recycler view
        exCurrent.setLayoutManager(new LinearLayoutManager(getContext()));
        queryWorkouts();

    }
    protected void queryWorkouts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Workout> query = ParseQuery.getQuery(Workout.class);
        // include data referred by user key
        query.include(Workout.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Workout>() {
            @Override
            public void done(List<Workout> workouts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Workout workout : workouts) {
                    Log.i(TAG, "Post: " + workout.getDescription() + ", username: " + workout.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allWorkouts.addAll(workouts);
                CurrentAdapter.notifyDataSetChanged();
            }
        });
    }
}