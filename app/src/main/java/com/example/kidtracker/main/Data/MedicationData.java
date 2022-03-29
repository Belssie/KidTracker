package com.example.kidtracker.main.Data;

public class MedicationData {
    String dateTimeMedication;
    String titleMedication;
    String amountMedication;
    String noteMedication;

    public MedicationData(String dateTimeMedication, String titleMedication, String amountMedication, String noteMedication) {
        this.dateTimeMedication = dateTimeMedication;
        this.titleMedication = titleMedication;
        this.amountMedication = amountMedication;
        this.noteMedication = noteMedication;
    }

    public MedicationData(){}

    @Override
    public String toString() {
        return "MedicationData{" +
                "dateTimeMedication='" + dateTimeMedication + '\'' +
                ", titleMedication='" + titleMedication + '\'' +
                ", amountMedication='" + amountMedication + '\'' +
                ", noteMedication='" + noteMedication + '\'' +
                '}';
    }

    public String getDateTimeMedication() {
        return dateTimeMedication;
    }

    public void setDateTimeMedication(String dateTimeMedication) {
        this.dateTimeMedication = dateTimeMedication;
    }

    public String getTitleMedication() {
        return titleMedication;
    }

    public void setTitleMedication(String titleMedication) {
        this.titleMedication = titleMedication;
    }

    public String getAmountMedication() {
        return amountMedication;
    }

    public void setAmountMedication(String amountMedication) {
        this.amountMedication = amountMedication;
    }

    public String getNoteMedication() {
        return noteMedication;
    }

    public void setNoteMedication(String noteMedication) {
        this.noteMedication = noteMedication;
    }
}
