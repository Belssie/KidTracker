package com.example.kidtracker.main.Data;

public class GrowthData {
    String dateTimeGrowth;
    String lengthGrowth;
    String weightGrowth;
    String noteGrowth;

    public GrowthData(String dateTimeGrowth, String lengthGrowth, String weightGrowth, String noteGrowth) {
        this.dateTimeGrowth = dateTimeGrowth;
        this.lengthGrowth = lengthGrowth;
        this.weightGrowth = weightGrowth;
        this.noteGrowth = noteGrowth;
    }

    public GrowthData(){}

    @Override
    public String toString() {
        return "GrowthData{" +
                "dateTimeGrowth='" + dateTimeGrowth + '\'' +
                ", lengthGrowth='" + lengthGrowth + '\'' +
                ", weightGrowth='" + weightGrowth + '\'' +
                ", noteGrowth='" + noteGrowth + '\'' +
                '}';
    }

    public String getDateTimeGrowth() {
        return dateTimeGrowth;
    }

    public void setDateTimeGrowth(String dateTimeGrowth) {
        this.dateTimeGrowth = dateTimeGrowth;
    }

    public String getLengthGrowth() {
        return lengthGrowth;
    }

    public void setLengthGrowth(String lengthGrowth) {
        this.lengthGrowth = lengthGrowth;
    }

    public String getWeightGrowth() {
        return weightGrowth;
    }

    public void setWeightGrowth(String weightGrowth) {
        this.weightGrowth = weightGrowth;
    }

    public String getNoteGrowth() {
        return noteGrowth;
    }

    public void setNoteGrowth(String noteGrowth) {
        this.noteGrowth = noteGrowth;
    }
}
