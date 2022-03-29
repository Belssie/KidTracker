package com.example.kidtracker.main.Data;

public class VaccineData {
    String dateTimeVaccine;
    String titleVaccine;
    String noteVaccine;

    public VaccineData(String dateTimeVaccine, String titleVaccine, String noteVaccine) {
        this.dateTimeVaccine = dateTimeVaccine;
        this.titleVaccine = titleVaccine;
        this.noteVaccine = noteVaccine;
    }

    public VaccineData(){}

    @Override
    public String toString() {
        return "VaccineData{" +
                "dateTimeVaccine='" + dateTimeVaccine + '\'' +
                ", titleVaccine='" + titleVaccine + '\'' +
                ", noteVaccine='" + noteVaccine + '\'' +
                '}';
    }

    public String getDateTimeVaccine() {
        return dateTimeVaccine;
    }

    public void setDateTimeVaccine(String dateTimeVaccine) {
        this.dateTimeVaccine = dateTimeVaccine;
    }

    public String getTitleVaccine() {
        return titleVaccine;
    }

    public void setTitleVaccine(String titleVaccine) {
        this.titleVaccine = titleVaccine;
    }

    public String getNoteVaccine() {
        return noteVaccine;
    }

    public void setNoteVaccine(String noteVaccine) {
        this.noteVaccine = noteVaccine;
    }
}
