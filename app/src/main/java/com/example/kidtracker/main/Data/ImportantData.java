package com.example.kidtracker.main.Data;

public class ImportantData {
    public String importantDateTime;
    public String importantType;

    public ImportantData(String importantDateTime, String importantType) {
        this.importantDateTime = importantDateTime;
        this.importantType = importantType;
    }

    public ImportantData(){}

    @Override
    public String toString() {
        return "ImportantData{" +
                "importantDateTime='" + importantDateTime + '\'' +
                ", importantType='" + importantType + '\'' +
                '}';
    }

    public String getImportantDateTime() {
        return importantDateTime;
    }

    public void setImportantDateTime(String importantDateTime) {
        this.importantDateTime = importantDateTime;
    }

    public String getImportantType() {
        return importantType;
    }

    public void setImportantType(String importantType) {
        this.importantType = importantType;
    }
}
