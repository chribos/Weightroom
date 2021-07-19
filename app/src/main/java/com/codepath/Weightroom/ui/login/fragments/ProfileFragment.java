package com.codepath.Weightroom.ui.login.fragments;

import android.util.Log;

import com.codepath.Weightroom.ui.login.Exercise;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends FeedFragment {

    public String TAG = "ProfileFragment";

    /**since the only hting that differs is the query, we are going to want to modify it so we must
     * make [query()] in FeedFragment protected instead of private so we can access it here.
     */
    @Override
    protected void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Exercise> query = ParseQuery.getQuery(Exercise.class);
        // include data referred by user key
        query.include(Exercise.KEY_USER);
        //filter by user profile instead of all posts!!!
        query.whereEqualTo(Exercise.KEY_USER, ParseUser.getCurrentUser());
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> exercises, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Exercise exercise : exercises) {
                    Log.i(TAG, "Post: " + exercise.getDescription() + ", username: " + exercise.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allExercises.addAll(exercises);
                ExercisesAdapter.notifyDataSetChanged();
            }
        });
    }
}
