package com.example.kidtracker.main.Data;

public class ExpressedMilkData {
    String dateTimeExpressed;
    String amountExpressed;
    String noteExpressed;

    public ExpressedMilkData(String dateTimeExpressed, String amountExpressed, String noteExpressed) {
        this.dateTimeExpressed = dateTimeExpressed;
        this.amountExpressed = amountExpressed;
        this.noteExpressed = noteExpressed;
    }

    public ExpressedMilkData(){}

    @Override
    public String toString() {
        return "ExpressedMilkData{" +
                "dateTimeExpressed='" + dateTimeExpressed + '\'' +
                ", amountExpressed='" + amountExpressed + '\'' +
                ", noteExpressed='" + noteExpressed + '\'' +
                '}';
    }

    public String getDateTimeExpressed() {
        return dateTimeExpressed;
    }

    public void setDateTimeExpressed(String dateTimeExpressed) {
        this.dateTimeExpressed = dateTimeExpressed;
    }

    public String getAmountExpressed() {
        return amountExpressed;
    }

    public void setAmountExpressed(String amountExpressed) {
        this.amountExpressed = amountExpressed;
    }

    public String getNoteExpressed() {
        return noteExpressed;
    }

    public void setNoteExpressed(String noteExpressed) {
        this.noteExpressed = noteExpressed;
    }
}
