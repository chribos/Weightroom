package com.codepath.Weightroom.ui.login.fragments;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;

import com.codepath.Weightroom.R;
import com.codepath.Weightroom.ui.login.Equipment;
import com.codepath.Weightroom.ui.login.Exercise;
import com.codepath.Weightroom.ui.login.ExercisesAdapter;

import com.codepath.Weightroom.ui.login.LoginActivity;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    public List userEquipment;
    public Button exLogout;

    //2 is the id for the english language, 1 for german
    public static final String LANGUAGE_KEY =  String.valueOf(2) ;
    //create instance variable for language [readability]
    public static final String EXERCISE_INFO_URL =
            "https://wger.de/api/v2/exerciseinfo/?format=json&language="+LANGUAGE_KEY +"&limit=40";

    private String TAG = "FeedFragment";
    protected ExercisesAdapter ExercisesAdapter;
    protected ImageView homeIcon;

    // the fragment initialization parameters
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
        homeIcon = view.findViewById(R.id.homeIcon);
        rvExercises = view.findViewById(R.id.rvExercises);
        exLogout = view.findViewById(R.id.exLogout);

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
        // set the layout manager on the recycler view
        rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));

    }
    protected void asyncCall() {
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