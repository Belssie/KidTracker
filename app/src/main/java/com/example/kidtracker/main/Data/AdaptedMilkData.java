package com.example.kidtracker.main.Data;

public class AdaptedMilkData {
    String dateTimeAm;
    String amountAm;
    String typeAm;
    String noteAm;

    public AdaptedMilkData(String dateTimeAm, String amountAm, String typeAm, String noteAm) {
        this.dateTimeAm = dateTimeAm;
        this.amountAm = amountAm;
        this.typeAm = typeAm;
        this.noteAm = noteAm;
    }

    public AdaptedMilkData(){}

    @Override
    public String toString() {
        return "AdaptedMilkData{" +
                "dateTimeAm='" + dateTimeAm + '\'' +
                ", amountAm='" + amountAm + '\'' +
                ", typeAm='" + typeAm + '\'' +
                ", noteAm='" + noteAm + '\'' +
                '}';
    }

    public String getDateTimeAm() {
        return dateTimeAm;
    }

    public void setDateTimeAm(String dateTimeAm) {
        this.dateTimeAm = dateTimeAm;
    }

    public String getAmountAm() {
        return amountAm;
    }

    public void setAmountAm(String amountAm) {
        this.amountAm = amountAm;
    }

    public String getTypeAm() {
        return typeAm;
    }

    public void setTypeAm(String typeAm) {
        this.typeAm = typeAm;
    }

    public String getNoteAm() {
        return noteAm;
    }

    public void setNoteAm(String noteAm) {
        this.noteAm = noteAm;
    }
}
