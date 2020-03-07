package com.example.fitnessapp.main;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.user.Exercise;

public class ExersiceFieldRecyclerAdapter extends RecyclerView.Adapter<ExersiceFieldRecyclerAdapter.FieldHolder> {

    private Exercise exercise;
    private LayoutInflater inflater;

    public ExersiceFieldRecyclerAdapter(Exercise exercise, LayoutInflater inflater) {
        this.exercise = exercise;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public FieldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.ex_activity_recycler,parent,false);
        return new FieldHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldHolder holder, int position) {

        holder.tvSets.setText(String.valueOf(position+1));
        if (position == exercise.getSets()-1){
            Resources res = holder.itemView.getContext().getResources();
            holder.line.setBackgroundColor(res.getColor(R.color.opacity));
        }


    }

    @Override
    public int getItemCount() {
        return exercise.getSets();
    }

    public class FieldHolder extends RecyclerView.ViewHolder{
        TextView tvSets;
        EditText etRepit;
        EditText etKG;
        View line;
        public FieldHolder(@NonNull View itemView) {
            super(itemView);

            tvSets = itemView.findViewById(R.id.ex_history_recycler_set);
            etRepit = itemView.findViewById(R.id.ex_activity_recycler_repit);
            etKG = itemView.findViewById(R.id.ex_activity_recycler_kg);
            line = itemView.findViewById(R.id.ex_activity_recycler_line);
        }
    }

    public String getExName(){
        return exercise.getExName();
    }

}
