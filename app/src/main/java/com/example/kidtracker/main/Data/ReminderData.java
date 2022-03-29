package com.example.kidtracker.main.Data;

public class ReminderData {
    public String title;
    public String time;
    public int broadCastId;

    public ReminderData(String title, String time, int broadCastId) {
        this.title = title;
        this.time = time;
        this.broadCastId = broadCastId;
    }

    public ReminderData(){}
    @Override
    public String toString() {
        return "ReminderData{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", broadCastId='" + broadCastId + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getBroadCastId() {
        return broadCastId;
    }

    public void setBroadCastId(int broadCastId) {
        this.broadCastId = broadCastId;
    }
}
