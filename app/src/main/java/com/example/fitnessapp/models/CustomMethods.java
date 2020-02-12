package com.example.fitnessapp.models;

import com.example.fitnessapp.user.Day;
import com.example.fitnessapp.user.Exercise;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CustomMethods {

    private CustomMethods(){

    }

    public static String getEsTime(Day day){
        List<Exercise> exercises = day.getExercises();
        long restTime = 0;
        long exTime = 0;
        for (Exercise exercise : exercises) {
            restTime += exercise.getRest();
            restTime += exercise.getSets() * 60_000;
        }
        long hours = TimeUnit.MILLISECONDS.toHours(restTime);
        long min = TimeUnit.MILLISECONDS.toMinutes(restTime) - (hours*60);
        String minString = String.valueOf(min);
        String hourString = String.valueOf(hours);
        String total;


        if (min < 10){
            total = hourString + ":0" + min;
        } else {
            total = hourString + ":" + min;
        }

        return total;

    }

    public static String getEsTime(long restTime){
        long hours = TimeUnit.MILLISECONDS.toHours(restTime);
        long min = TimeUnit.MILLISECONDS.toMinutes(restTime) - (hours*60);
        String minString = String.valueOf(min);
        String hourString = String.valueOf(hours);
        String total;


        if (min < 10){
            total = hourString + ":0" + min;
        } else {
            total = hourString + ":" + min;
        }

        return total;
    }

    public static String convertDateToHebrew(String correctDay){
        switch (correctDay){
            case "sunday":
                return "יום ראשון";
            case "monday":
                return "יום שני";
            case "tuesday":
                return "יום שלישי";
            case "wednesday":
                return "יום רביעי";
            case "thursday":
                return "יום חמישי";
            case "friday":
                return "יום שישי";
            case "saturday":
                return "יום שבת";
        }

        return null;
    }
}
