package com.example.fitnessapp.user;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DietProcessRaw implements Serializable {
    private Date date;
    private float weight;
    private float abdominal;
    private float arm;
    private float bodyFat;
    private String dateString;

    public DietProcessRaw(Date date, float weight, float abdominal, float arm, float bodyFat, String dateString) {
        this.date = date;
        this.weight = weight;
        this.abdominal = abdominal;
        this.arm = arm;
        this.bodyFat = bodyFat;
        this.dateString = dateString;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getAbdominal() {
        return abdominal;
    }

    public void setAbdominal(float abdominal) {
        this.abdominal = abdominal;
    }

    public float getArm() {
        return arm;
    }

    public void setArm(float arm) {
        this.arm = arm;
    }

    public float getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(float bodyFat) {
        this.bodyFat = bodyFat;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public String toString() {
        return "DietProcessRaw{" +
                "date='" + date + '\'' +
                ", weight='" + weight + '\'' +
                ", abdominal='" + abdominal + '\'' +
                ", arm='" + arm + '\'' +
                ", bodyFat='" + bodyFat + '\'' +
                ", dateString='" + dateString + '\'' +
                '}';
    }
}
