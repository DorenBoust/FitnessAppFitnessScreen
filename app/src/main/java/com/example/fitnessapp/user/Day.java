package com.example.fitnessapp.user;

import java.io.Serializable;
import java.util.List;

public class Day implements Serializable {
    private String dayName;
    private List<Exercise> exercises;

    public Day(String dayName, List<Exercise> exercises) {
        this.dayName = dayName;
        this.exercises = exercises;
    }

    public String getDayName() {
        return dayName;
    }
    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
    public List<Exercise> getExercises() {
        return exercises;
    }
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public String toString() {
        return "Day{" +
                "dayName='" + dayName + '\'' +
                ", exercises=" + exercises +
                '}';
    }
}
