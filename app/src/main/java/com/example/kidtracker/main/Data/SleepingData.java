package com.example.kidtracker.main.Data;

public class SleepingData {
    public String fellAsleep;
    public String wokeUp;
    public String noteSleep;

    public SleepingData(String fellAsleep, String wokeUp, String noteSleep) {
        this.fellAsleep = fellAsleep;
        this.wokeUp = wokeUp;
        this.noteSleep = noteSleep;
    }

    public SleepingData(){}

    @Override
    public String toString() {
        return "SleepingData{" +
                "fellAsleep='" + fellAsleep + '\'' +
                ", wokeUp='" + wokeUp + '\'' +
                ", noteSleep='" + noteSleep + '\'' +
                '}';
    }

    public String getFellAsleep() {
        return fellAsleep;
    }

    public void setFellAsleep(String fellAsleep) {
        this.fellAsleep = fellAsleep;
    }

    public String getWokeUp() {
        return wokeUp;
    }

    public void setWokeUp(String wokeUp) {
        this.wokeUp = wokeUp;
    }

    public String getNoteSleep() {
        return noteSleep;
    }

    public void setNoteSleep(String noteSleep) {
        this.noteSleep = noteSleep;
    }
}
