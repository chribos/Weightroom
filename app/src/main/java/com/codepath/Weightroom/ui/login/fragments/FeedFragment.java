package com.codepath.Weightroom.ui.login.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codepath.Weightroom.R;
import com.codepath.Weightroom.ui.login.Exercise;
import com.codepath.Weightroom.ui.login.ExercisesAdapter;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    protected List<Exercise> allExercises;
    public RecyclerView rvExercises;

    //2 is the id for the english language, 1 for german
    public static final String LANGUAGE_KEY =  String.valueOf(2) ;
    //create instance variable for language [readability]
    public static final String EXERCISE_INFO_URL =
            "https://wger.de/api/v2/exerciseinfo/?format=json&language="+LANGUAGE_KEY;

    private String TAG = "FeedFragment";
    protected ExercisesAdapter ExercisesAdapter;
    protected ImageView homeIcon;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedFragment() {
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
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //refresh
        // Lookup the swipe container view
        homeIcon = view.findViewById(R.id.homeIcon);
        // Setup refresh listener which triggers new data loading
        rvExercises = view.findViewById(R.id.rvExercises);

        // initialize the array that will hold exercices and create a PostsAdapter
        allExercises = new ArrayList<>();
        ExercisesAdapter = new ExercisesAdapter(getContext(), allExercises);


        // set the adapter on the recycler view
        rvExercises.setAdapter(ExercisesAdapter);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(EXERCISE_INFO_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject= json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results" + results.toString());
                    allExercises.addAll(Exercise.fromJsonArray(results));
                    ExercisesAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Exercises" + allExercises.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
        // set the layout manager on the recycler view
        rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        // query posts from Parstagram
//        queryPosts();
    }

//    protected void queryPosts() {
//        // specify what type of data we want to query - Post.class
//        ParseQuery<Exercise> query = ParseQuery.getQuery(Exercise.class);
//        // include data referred by user key
//        query.include(Exercise.KEY_USER);
//        // limit query to latest 20 items
//        query.setLimit(20);
//        // order posts by creation date (newest first)
//        query.addDescendingOrder("createdAt");
//        // start an asynchronous call for posts
//        query.findInBackground(new FindCallback<Exercise>() {
//            @Override
//            public void done(List<Exercise> exercises, ParseException e) {
//                // check for errors
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting posts", e);
//                    return;
//                }
//
//                // for debugging purposes let's print every post description to logcat
//                for (Exercise exercise : exercises) {
//                    Log.i(TAG, "Post: " + exercise.getDescription() + ", username: " + exercise.getUser().getUsername());
//                }
//
//                // save received posts to list and notify adapter of new data
//                allExercises.addAll(exercises);
//                ExercisesAdapter.notifyDataSetChanged();
//            }
//        });
//    }
}