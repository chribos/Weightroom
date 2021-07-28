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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.ViewHolder> {
    private Context context;
    private List<Workout> workouts;


    public void clear() {
        workouts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Workout> list) {
        workouts.addAll(list);
        notifyDataSetChanged();
    }

    public CurrentAdapter(Context context, List<Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.bind(workout);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exTitle;
        private TextView exEquipment;
        private RecyclerView exCurrent;
        private TextView exCategory;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exTitle = itemView.findViewById(R.id.exTitle);
            exEquipment = itemView.findViewById(R.id.exEquipment);
            exCurrent = itemView.findViewById(R.id.exCurrent);
            exCategory = itemView.findViewById(R.id.exCategory);


        }

        public void bind(Workout workout) {
            // Bind the post data to the view elements
            exTitle.setText(workout.getTitle());
            exEquipment.setText("Equipment: "+workout.getEquipment());
            exCategory.setText("Category: "+ workout.getCategory());
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    Log.i("Adapter", "item has been long-clicked");
//                    ParseObject workoutClass = new ParseObject("Workout");
//                    workoutClass.put("exTitle", exercise.getExTitle());
//                    workoutClass.put("exDescription", exercise.getExDescription());
//                    workoutClass.put("exCategory", exercise.getExCategory());
//                    workoutClass.put("exEquipment", exercise.getExEquipment());
//                    workoutClass.put("user", ParseUser.getCurrentUser());
//
//                    workoutClass.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if(e != null) {
//                                Log.e("LongClick", "error:" +e);
//                            } else{
//                                Log.i("LongClick", "Exercise saved!");
//                            }
//                        }
//                    });
//
//                    return false;
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {

                        Intent i = new Intent(context, DetailsActivity.class);
                        Log.i("CurrentAdapter", workout.getDescription());
                        i.putExtra("w", Parcels.wrap(workout));
                        context.startActivity(i);

                    }
                }
            });
        }

    }
}

