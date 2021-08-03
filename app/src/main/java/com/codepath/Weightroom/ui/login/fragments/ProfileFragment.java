package com.codepath.Weightroom.ui.login.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.Weightroom.R;
import com.codepath.Weightroom.ui.login.Equipment;
import com.codepath.Weightroom.ui.login.Exercise;
import com.codepath.Weightroom.ui.login.ExercisesAdapter;
import com.codepath.Weightroom.ui.login.LoginActivity;
import com.codepath.Weightroom.ui.login.MainActivity;
import com.codepath.Weightroom.ui.login.PermaPromptActivity;
import com.codepath.Weightroom.ui.login.PromptActivity;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;


public class ProfileFragment extends Fragment {

    public String TAG = "ProfileFragment";


    protected TextView exUsername;
    protected ImageButton exLogout;
    protected CircleImageView ivProfile;
    protected  TextView eqList;
    protected ImageButton exEdit;


    protected com.codepath.Weightroom.ui.login.ExercisesAdapter ExercisesAdapter;
    protected ImageView homeIcon;

    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeIcon = view.findViewById(R.id.homeIcon);
        ivProfile = view.findViewById(R.id.ivProfile);
        exUsername = view.findViewById(R.id.exUsername);
        exLogout = view.findViewById(R.id.exLogout);
        eqList = view.findViewById(R.id.eqList);
        exEdit = view.findViewById(R.id.exEdit);


        String currentUsername = ParseUser.getCurrentUser().getUsername();
        exUsername.setText(currentUsername);

        //queries for user's list of equipment specified from prompt
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
                //iterate through posts if successful and
                eqList.setText(posts.get(0).getEquipment().toString());
                Log.i(TAG, "equipment: " + posts.get(0).getEquipment().toString());
            }
        });



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

        exEdit.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PermaPromptActivity.class);
                startActivity(intent);

            }
        });
    }
}


