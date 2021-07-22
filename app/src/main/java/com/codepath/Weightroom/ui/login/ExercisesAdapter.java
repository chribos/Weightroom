package com.codepath.Weightroom.ui.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
//            Date createdAt = exercise.getCreatedAt();
//            String timeAgo = Exercise.calculateTimeAgo(createdAt);
            // Bind the post data to the view elements
            exTitle.setText(exercise.getExTitle());
            exEquipment.setText("Equipment: "+exercise.getExEquipment());
            exCategory.setText("Category: "+ exercise.getExCategory());
//            ParseFile image = exercise.getImage();
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
}
