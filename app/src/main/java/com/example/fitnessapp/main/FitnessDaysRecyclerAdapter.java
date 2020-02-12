package com.example.fitnessapp.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.CustomMethods;
import com.example.fitnessapp.user.Day;
import com.example.fitnessapp.user.Exercise;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FitnessDaysRecyclerAdapter extends RecyclerView.Adapter<FitnessDaysRecyclerAdapter.DaysHolder> {

    List<Day> days;
    LayoutInflater inflater;

    //correct day
    private SimpleDateFormat sdf;
    private Date date = new Date();
    private String correctDay;


    public FitnessDaysRecyclerAdapter(List<Day> days, LayoutInflater inflater) {
        this.days = days;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public DaysHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.fitness_days_recycler, parent,false);
        sdf = new SimpleDateFormat("EEEE");
        correctDay = sdf.format(date).toLowerCase();

        return new DaysHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysHolder holder, int position) {

        //set name day
        Day day = days.get(position);
        String dayName = day.getDayName();

        switch (dayName){
            case "sunday":
                holder.dayName.setText("יום ראשון");
                break;
            case "monday":
                holder.dayName.setText("יום שני");
                break;
            case "tuesday":
                holder.dayName.setText("יום שלישי");
                break;
            case "wednesday":
                holder.dayName.setText("יום רביעי");
                break;
            case "thursday":
                holder.dayName.setText("יום חמישי");
                break;
            case "friday":
                holder.dayName.setText("יום שישי");
                break;
            case "saturday":
                holder.dayName.setText("יום שבת");
                break;
        }

        String esTime = CustomMethods.getEsTime(day);

        holder.esTime.setText(esTime);

        //set ex numbers
        List<Exercise> exercises = day.getExercises();
        holder.numberOfEx.setText(String.valueOf(exercises.size()));

    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class DaysHolder extends RecyclerView.ViewHolder{
        TextView dayName;
        TextView esTime;
        TextView esTimeTitle;
        TextView numberOfEx;
        TextView numberOfExTitle;


        public DaysHolder(@NonNull View itemView) {
            super(itemView);

            dayName = itemView.findViewById(R.id.tv_recycler_dayname);
            esTime = itemView.findViewById(R.id.tv_recycler_esTime);
            esTimeTitle = itemView.findViewById(R.id.tv_recycler_esTime_title);
            numberOfEx = itemView.findViewById(R.id.tv_recycler_exNumber);
            numberOfExTitle = itemView.findViewById(R.id.tv_recycler_numberOfEx_title);
        }
    }


}
