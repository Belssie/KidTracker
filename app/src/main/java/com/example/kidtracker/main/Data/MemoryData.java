package com.example.kidtracker.main.Data;

public class MemoryData {
    String dateTimeMemory;
    String titleMemory;
    String noteMemory;

    public MemoryData(String dateTimeMemory, String titleMemory, String noteMemory) {
        this.dateTimeMemory = dateTimeMemory;
        this.titleMemory = titleMemory;
        this.noteMemory = noteMemory;
    }

    public MemoryData(){}

    @Override
    public String toString() {
        return "MemoryData{" +
                "dateTimeMemory='" + dateTimeMemory + '\'' +
                ", titleMemory='" + titleMemory + '\'' +
                ", noteMemory='" + noteMemory + '\'' +
                '}';
    }

    public String getDateTimeMemory() {
        return dateTimeMemory;
    }

    public void setDateTimeMemory(String dateTimeMemory) {
        this.dateTimeMemory = dateTimeMemory;
    }

    public String getTitleMemory() {
        return titleMemory;
    }

    public void setTitleMemory(String titleMemory) {
        this.titleMemory = titleMemory;
    }

    public String getNoteMemory() {
        return noteMemory;
    }

    public void setNoteMemory(String noteMemory) {
        this.noteMemory = noteMemory;
    }
}
