package com.codepath.Weightroom.ui.login;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.Weightroom.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exercises;


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
//        previously name of the user, now title of the exercise, exTitle
//        private TextView exName;
        private TextView exTitle;
        private TextView exDescription;
        private LinearLayout tvMedia;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exTitle = itemView.findViewById(R.id.exTitle);
            exDescription = itemView.findViewById(R.id.exDescription);
            tvMedia = itemView.findViewById(R.id.tvMedia);

        }

        public void bind(Exercise exercise) {
            Date createdAt = exercise.getCreatedAt();
            String timeAgo = Exercise.calculateTimeAgo(createdAt);
            // Bind the post data to the view elements
            exDescription.setText(exercise.getDescription());
            exTitle.setText(exercise.getExTitle());
            ParseFile image = exercise.getImage();
            tvMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("post", Parcels.wrap(exercise));
                    context.startActivity(i);
                }
            });
        }

    }
}
