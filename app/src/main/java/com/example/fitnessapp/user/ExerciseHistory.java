package com.example.fitnessapp.user;

import java.util.Date;
import java.util.List;

public class ExerciseHistory {
    private String date;
    private List<ExersixeOneRawHistory> exList;

    public ExerciseHistory(String date, List<ExersixeOneRawHistory> exList) {
        this.date = date;
        this.exList = exList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ExersixeOneRawHistory> getExList() {
        return exList;
    }

    public void setExList(List<ExersixeOneRawHistory> exList) {
        this.exList = exList;
    }

    @Override
    public String toString() {
        return "ExerciseHistory{" +
                "date='" + date + '\'' +
                ", exList=" + exList +
                '}';
    }
}
