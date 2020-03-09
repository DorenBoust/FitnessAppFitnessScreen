package com.example.fitnessapp.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.user.ExerciseHistory;
import com.example.fitnessapp.user.ExersixeOneRawHistory;

import java.util.List;

public class ExsercieHistoryRecyclerAdapter extends RecyclerView.Adapter<ExsercieHistoryRecyclerAdapter.HistoryFieldHolder> {

    private List<ExerciseHistory> exerciseHistoryRoot;
    private LayoutInflater inflater;
    private int dateCounter;


    public ExsercieHistoryRecyclerAdapter(List<ExerciseHistory> exerciseHistory, int dateCounter, LayoutInflater inflater) {
        this.exerciseHistoryRoot = exerciseHistory;
        this.dateCounter = dateCounter;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public HistoryFieldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.ex_history_recycler,parent,false);
        return new HistoryFieldHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryFieldHolder holder, int position) {
        holder.set.setText(String.valueOf(position+1));
        ExerciseHistory exerciseHistory = exerciseHistoryRoot.get(dateCounter);
        List<ExersixeOneRawHistory> exList = exerciseHistory.getExList();
        for (int i = 0; i < exList.size() ; i++) {
            ExersixeOneRawHistory exersixeOneRawHistory = exList.get(position);
            holder.repit.setText(String.valueOf(exersixeOneRawHistory.getRepit()));
            holder.kg.setText(String.valueOf(exersixeOneRawHistory.getKg()));
        }

    }

    @Override
    public int getItemCount() {
        ExerciseHistory exerciseHistory = exerciseHistoryRoot.get(dateCounter);
        List<ExersixeOneRawHistory> exList = exerciseHistory.getExList();

        return exList.size();
    }

    public class HistoryFieldHolder extends RecyclerView.ViewHolder {
        TextView set;
        TextView repit;
        TextView kg;
        View line;

        public HistoryFieldHolder(@NonNull View itemView) {
            super(itemView);
            set = itemView.findViewById(R.id.ex_history_recycler_set);
            repit = itemView.findViewById(R.id.ex_history_recycler_repit);
            kg = itemView.findViewById(R.id.ex_history_recycler_kg);
            line = itemView.findViewById(R.id.ex_activity_recycler_line);

        }

    }
}


