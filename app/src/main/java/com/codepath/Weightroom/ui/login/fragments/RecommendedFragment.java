package com.codepath.Weightroom.ui.login.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codepath.Weightroom.ui.login.ExercisesAdapter;

import org.jetbrains.annotations.NotNull;

public class RecommendedFragment extends FeedFragment{
    //when switch is clicked this activity is started
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        ExercisesAdapter.clear();
    }
    //clear previous adapter and do a query for isRecommended posts in Parse after we add rules
    // to and add workouts to the Workout class in parse with isRecommended set to true
//    add filter parameter to keep these posts out of the current list.
    // and populate the recycle view

    //set another switch listener at the end to go back to FeedFragment

}
