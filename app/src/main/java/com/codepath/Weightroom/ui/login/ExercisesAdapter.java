package com.codepath.Weightroom.ui.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.Weightroom.R;
import com.codepath.Weightroom.ui.login.fragments.ComposeFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exercises;
    public List<Workout> checkWorkouts;
    public final String TAG = "ExerciseAdapter";


    public void clear() {
        exercises.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Exercise> list) {
        exercises.addAll(list);
        notifyDataSetChanged();
    }

    public ExercisesAdapter(Context context, List<Exercise> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.bind(exercise);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exTitle;
        private TextView exEquipment;
        private LinearLayout tvMedia;
        private TextView exCategory;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exTitle = itemView.findViewById(R.id.exTitle);
            exEquipment = itemView.findViewById(R.id.exEquipment);
            tvMedia = itemView.findViewById(R.id.tvMedia);
            exCategory = itemView.findViewById(R.id.exCategory);


        }

        public void bind(Exercise exercise) {
            // Bind the post data to the view elements
            exTitle.setText(exercise.getExTitle());
            exEquipment.setText("Equipment: "+exercise.getExEquipment());
            exCategory.setText("Category: "+ exercise.getExCategory());

//            //query workouts to check for duplicates after longClick
//            queryWorkouts();

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("Adapter", "item has been long-clicked");
                    checkWorkouts = new ArrayList<>();
                    //query workouts to check for duplicates after longClick
                    queryWorkouts();
                    Log.i(TAG, checkWorkouts.toString());
                    Log.i(TAG, "exercise title"+ exercise.getExTitle());

                    //make query and make sure there are no duplicates and show toast
                    Workout workoutClass = new Workout();
                    workoutClass.setUser(ParseUser.getCurrentUser());
                    workoutClass.put("exTitle", exercise.getExTitle());
                    workoutClass.put("exDescription", exercise.getExDescription());
                    workoutClass.put("exCategory", exercise.getExCategory());
                    workoutClass.put("exEquipment", exercise.getExEquipment());
                    workoutClass.put("user", ParseUser.getCurrentUser());

                    workoutClass.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e("LongClick", "error:" + e);
                            } else {
                                Toast.makeText(context, "Exercise added to current exercise list", Toast.LENGTH_SHORT).show();
                                Log.i("LongClick workouts", checkWorkouts.toString());
                            }
                        }
                    });

                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        Intent i = new Intent(context, DetailsActivity.class);
                        Log.i("ExerciseAdapter", exercise.getExDescription());
                        i.putExtra("e", Parcels.wrap(exercise));
                        context.startActivity(i);

                    }
                }
            });
        }

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
                    Log.e(TAG, "Issue with getting exercises", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Workout workout : workouts) {
                    Log.i(TAG, "Workout: " + workout.getTitle() + ", username: " + workout.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                checkWorkouts.addAll(workouts);
                notifyDataSetChanged();
            }
        });
    }
}
