package com.example.kidtracker.main.Data;

public class DiaperData {
    String dateTimeDiaper;
    String typeDiaper;
    String noteDiaper;

    public DiaperData(String dateTimeDiaper, String typeDiaper, String noteDiaper) {
        this.dateTimeDiaper = dateTimeDiaper;
        this.typeDiaper = typeDiaper;
        this.noteDiaper = noteDiaper;
    }

    public DiaperData(){}

    @Override
    public String toString() {
        return "DiaperData{" +
                "dateTimeDiaper='" + dateTimeDiaper + '\'' +
                ", typeDiaper='" + typeDiaper + '\'' +
                ", noteDiaper='" + noteDiaper + '\'' +
                '}';
    }

    public String getDateTimeDiaper() {
        return dateTimeDiaper;
    }

    public void setDateTimeDiaper(String dateTimeDiaper) {
        this.dateTimeDiaper = dateTimeDiaper;
    }

    public String getTypeDiaper() {
        return typeDiaper;
    }

    public void setTypeDiaper(String typeDiaper) {
        this.typeDiaper = typeDiaper;
    }

    public String getNoteDiaper() {
        return noteDiaper;
    }

    public void setNoteDiaper(String noteDiaper) {
        this.noteDiaper = noteDiaper;
    }
}
