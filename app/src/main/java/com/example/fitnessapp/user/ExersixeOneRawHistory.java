package com.example.fitnessapp.user;

public class ExersixeOneRawHistory {
    private int set;
    private int repit;
    private double kg;

    public ExersixeOneRawHistory(int set, int repit, double kg) {
        this.set = set;
        this.repit = repit;
        this.kg = kg;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getRepit() {
        return repit;
    }

    public void setRepit(int repit) {
        this.repit = repit;
    }

    public double getKg() {
        return kg;
    }

    public void setKg(double kg) {
        this.kg = kg;
    }

    @Override
    public String toString() {
        return "ExersixeOneRawHistory{" +
                "set=" + set +
                ", repit=" + repit +
                ", kg=" + kg +
                '}';
    }
}
