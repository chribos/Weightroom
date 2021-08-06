package com.codepath.Weightroom.ui.login.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.codepath.Weightroom.R;
import com.codepath.Weightroom.ui.login.Equipment;
import com.codepath.Weightroom.ui.login.Exercise;
import com.codepath.Weightroom.ui.login.ExercisesAdapter;

import com.codepath.Weightroom.ui.login.LoginActivity;
import com.codepath.Weightroom.ui.login.RegisterActivity;
import com.codepath.Weightroom.ui.login.Workout;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public List userEquipment;
    public ImageButton exLogout;
    public Switch switchRecommended;
    protected List<Workout> recommendedWorkouts;

    //2 is the id for the english language, 1 for german
    public static final String LANGUAGE_KEY =  String.valueOf(2) ;
    //create instance variable for language [readability]
    public static final String EXERCISE_INFO_URL =
            "https://wger.de/api/v2/exerciseinfo/?format=json&language="+LANGUAGE_KEY +"&limit=50";

    private String TAG = "FeedFragment";
    protected ExercisesAdapter ExercisesAdapter;
    protected ImageView homeIcon;

    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        homeIcon = view.findViewById(R.id.homeIcon);
        rvExercises = view.findViewById(R.id.rvExercises);
        exLogout = view.findViewById(R.id.exLogout);
        switchRecommended = view.findViewById(R.id.switchRecommended);

        recommendedWorkouts = new ArrayList<>();

        // initialize the array that will hold exercises and create a PostsAdapter
        allExercises = new ArrayList<>();
        ExercisesAdapter = new ExercisesAdapter(getContext(), allExercises);

        //query for equipment list
        ParseQuery<Equipment> query = ParseQuery.getQuery(Equipment.class);
        query.include(Equipment.KEY_USER);
        query.whereEqualTo(Equipment.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Equipment>() {
            @Override
            public void done(List<Equipment> posts, ParseException e) {
                if (e!= null) {
                    Log.e(TAG, "issue with getting posts", e);
                    return;
                }
                //retrieves current user's equipment list
                userEquipment= posts.get(0).getEquipment();
                Log.i(TAG, "equipment: " + posts.get(0).getEquipment().toString());
            }
        });
        // set the adapter on the recycler view of exercises that contain user's equipment
        rvExercises.setAdapter(ExercisesAdapter);

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
        asyncCall();

        //when switch is checked
        switchRecommended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                Log.i("switch", "switch clicked!");
                allExercises.clear();
                ExercisesAdapter.notifyDataSetChanged();
                try {
                    asyncRecommendedCall();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //when switch is unchecked
                if (!switchRecommended.isChecked()){
                    // do something, the isChecked will be
                    // true if the switch is in the On position
                    Log.i("switch", "switch turned off");
                    //repopulate recycle view
                    asyncCall();
                }
            }
        });
        // set the layout manager on the recycler view
        rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //makes sure exercises on homeFeed contain user's equipment
    protected void asyncCall() {
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Wait while getting exercises...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(EXERCISE_INFO_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject= json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results" + results.toString());
                    Log.i(TAG, "Results" + Exercise.fromJsonArray(results).get(0).getExEquipment());
                    //only adds exercises that contain the user's equipment
                    for(int k =0; k<Exercise.fromJsonArray(results).size(); k++) {
                        for (int j = 0; j < userEquipment.size(); j++) {
                            Log.i(TAG, "eq check" + userEquipment.get(j).toString());
                            if (Exercise.fromJsonArray(results).get(k).getExEquipment().contains(userEquipment.get(j).toString())) {
                                allExercises.add(Exercise.fromJsonArray(results).get(k));
                                Log.i(TAG, "filtered exercises" + allExercises);
                            }
                        }
                    }
                    ExercisesAdapter.notifyDataSetChanged();
                    progress.hide();
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
    }

    //find out users most common category
    protected String getRecommendedCategory() throws ParseException {
        List<Pair<String, Integer>> workoutPair = new ArrayList<>();

        workoutPair.add(queryWhereEqual("Chest"));
        workoutPair.add(queryWhereEqual("Arms"));
        workoutPair.add(queryWhereEqual("Shoulders"));
        workoutPair.add(queryWhereEqual("Legs"));
        workoutPair.add(queryWhereEqual("Back"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(workoutPair, Comparator.comparing(p -> -p.second));
        }
        Log.i("workoutPair", workoutPair.toString());
        return workoutPair.get(0).first;
    }

    //this functions cuts down a ton of lines for getRecommendedCategory
    protected Pair<String, Integer> queryWhereEqual(String category) throws ParseException {
        ParseQuery<Workout> query = ParseQuery.getQuery(Workout.class);
        // include data referred by user key
        query.include(Workout.KEY_USER);
        query.whereEqualTo(Workout.KEY_USER, ParseUser.getCurrentUser());
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        query.whereEqualTo("exCategory", category);
        query.whereEqualTo("exCategory", category);
        // start an asynchronous call for posts
        return new Pair(category, query.count());
    }

    //filter for the user's most common category
    protected void asyncRecommendedCall() throws ParseException {
        String recommendedCategory = getRecommendedCategory();
        ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Wait while getting exercises...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(EXERCISE_INFO_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "Recommended onSuccess");
                JSONObject jsonObject= json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.d(TAG, "Recommended results:" +results);
                    //only adds exercises that contain the user's equipment
                    for(int k =0; k<Exercise.fromJsonArray(results).size(); k++) {
                        for (int j = 0; j < userEquipment.size(); j++) {
                            Log.i(TAG, "eq check" + userEquipment.get(j).toString());
                            if (Exercise.fromJsonArray(results).get(k).getExEquipment().contains(userEquipment.get(j).toString())) {
                                //matching category parameter
                                if(Exercise.fromJsonArray(results).get(k).getExCategory().equals(recommendedCategory)) {
                                    allExercises.add(Exercise.fromJsonArray(results).get(k));
                                    Log.i(TAG, "filtered recommended exercises" + allExercises);
                                    Log.i(TAG, "RecommendedCategoryResults" + Exercise.fromJsonArray(results).get(0).getExCategory());
                                }
                            }
                        }
                    }
                    ExercisesAdapter.notifyDataSetChanged();
                    progress.hide();
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
    }
}