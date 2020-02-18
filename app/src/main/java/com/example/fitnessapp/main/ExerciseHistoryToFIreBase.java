package com.example.fitnessapp.main;

import com.example.fitnessapp.user.ExerciseHistory;

import java.util.List;

public class ExerciseHistoryToFIreBase {
    private List<ExerciseHistory> exerciseHistories;

    public ExerciseHistoryToFIreBase(List<ExerciseHistory> exerciseHistories) {
        this.exerciseHistories = exerciseHistories;
    }

    public List<ExerciseHistory> getExerciseHistories() {
        return exerciseHistories;
    }

    public void setExerciseHistories(List<ExerciseHistory> exerciseHistories) {
        this.exerciseHistories = exerciseHistories;
    }

    @Override
    public String toString() {
        return "ExerciseHistoryToFIreBase{" +
                "exerciseHistories=" + exerciseHistories +
                '}';
    }
}
