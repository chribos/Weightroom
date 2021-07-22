package com.codepath.Weightroom.ui.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {
    public interface OnLongClickListener {
        //needs to now position of long press to notify adapter of deletion position
        void onItemLongClicked (int position);
    }
    //add OnLongClickListener to input of items adapter
    List<String> equipmentList;
    OnLongClickListener longClickListener;
    public EquipmentAdapter(List<String> equipmentList, OnLongClickListener longClickListener) {
        this.equipmentList = equipmentList;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate view using LayoutInflater
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //wrap in ViewHolder and return
        return new ViewHolder(todoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get item at position
        String equipment = equipmentList.get(position);
        //bind item to specified view holder
        holder.bind(equipment);

    }
    //gets total number of items in the list for the rv
    @Override
    public int getItemCount() {
        //string list length
        return equipmentList.size();
    }

    // Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {
        //TextView is found in simple_list_item_1 (hold command and click on it to see wrap)
        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        //update view inside ViewHolder with this item data
        public void bind(String equipment) {
            tvItem.setText(equipment);
            //add remove item functionality
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //removes the item from the rv
                    longClickListener.onItemLongClicked(getAdapterPosition()); //Notifies which position was long-pressed
                    return false;
                    //go back and communicate this to main activity to actually remove
                    // make interface at top to do this
                }
            });
        }
    }
}
