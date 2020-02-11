package com.example.fitnessapp.user;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String exName;
    private int sets;
    private String repitition;
    private long rest;
    private String notes;
    private String image;
    private String videoLink;

    public Exercise(String exName, int sets, String repitition, long rest, String notes, String image, String videoLink) {
        this.exName = exName;
        this.sets = sets;
        this.repitition = repitition;
        this.rest = rest;
        this.notes = notes;
        this.image = image;
        this.videoLink = videoLink;
    }

    public String getExName() {
        return exName;
    }
    public void setExName(String exName) {
        this.exName = exName;
    }
    public int getSets() {
        return sets;
    }
    public void setSets(int sets) {
        this.sets = sets;
    }
    public String getRepitition() {
        return repitition;
    }
    public void setRepitition(String repitition) {
        this.repitition = repitition;
    }
    public long getRest() {
        return rest;
    }
    public void setRest(long rest) {
        this.rest = rest;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getVideoLink() {
        return videoLink;
    }
    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exName='" + exName + '\'' +
                ", sets='" + sets + '\'' +
                ", repitition='" + repitition + '\'' +
                ", rest='" + rest + '\'' +
                ", notes='" + notes + '\'' +
                ", image='" + image + '\'' +
                ", videoLink='" + videoLink + '\'' +
                '}';
    }
}
