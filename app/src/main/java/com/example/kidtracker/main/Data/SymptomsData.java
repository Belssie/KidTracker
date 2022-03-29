package com.example.kidtracker.main.Data;

public class SymptomsData {
    String dateTimeSymptom;
    String titleSymptom;
    String noteSymptom;

    public SymptomsData(String dateTimeSymptom, String titleSymptom, String noteSymptom) {
        this.dateTimeSymptom = dateTimeSymptom;
        this.titleSymptom = titleSymptom;
        this.noteSymptom = noteSymptom;
    }

    public SymptomsData(){}

    @Override
    public String toString() {
        return "SymptomsData{" +
                "dateTimeSymptom='" + dateTimeSymptom + '\'' +
                ", titleSymptom='" + titleSymptom + '\'' +
                ", noteSymptom='" + noteSymptom + '\'' +
                '}';
    }

    public String getDateTimeSymptom() {
        return dateTimeSymptom;
    }

    public void setDateTimeSymptom(String dateTimeSymptom) {
        this.dateTimeSymptom = dateTimeSymptom;
    }

    public String getTitleSymptom() {
        return titleSymptom;
    }

    public void setTitleSymptom(String titleSymptom) {
        this.titleSymptom = titleSymptom;
    }

    public String getNoteSymptom() {
        return noteSymptom;
    }

    public void setNoteSymptom(String noteSymptom) {
        this.noteSymptom = noteSymptom;
    }
}
