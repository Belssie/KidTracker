package com.example.kidtracker.main.Data;

public class PumpingBreastFeedingData {
    String dateTimeStarted;
    String breastStarted;
    String amountPBF;
    String notePbm;
    String dateTimeEnded;
    String leftDur;
    String rightDur;

    public PumpingBreastFeedingData(String dateTimeStarted, String breastStarted, String amountPBF, String notePbm, String dateTimeEnded, String leftDur, String rightDur) {
        this.dateTimeStarted = dateTimeStarted;
        this.breastStarted = breastStarted;
        this.amountPBF = amountPBF;
        this.notePbm = notePbm;
        this.dateTimeEnded = dateTimeEnded;
        this.leftDur = leftDur;
        this.rightDur = rightDur;
    }

    public PumpingBreastFeedingData(){}

    @Override
    public String toString() {
        return "PumpingBreastFeedingData{" +
                "dateTimeStarted='" + dateTimeStarted + '\'' +
                ", breastStarted='" + breastStarted + '\'' +
                ", amountPBF='" + amountPBF + '\'' +
                ", notePbm='" + notePbm + '\'' +
                ", dateTimeEnded='" + dateTimeEnded + '\'' +
                ", leftDur='" + leftDur + '\'' +
                ", rightDur='" + rightDur + '\'' +
                '}';
    }

    public String getDateTimeStarted() {
        return dateTimeStarted;
    }

    public void setDateTimeStarted(String dateTimeStarted) {
        this.dateTimeStarted = dateTimeStarted;
    }

    public String getBreastStarted() {
        return breastStarted;
    }

    public void setBreastStarted(String breastStarted) {
        this.breastStarted = breastStarted;
    }

    public String getAmountPBF() {
        return amountPBF;
    }

    public void setAmountPBF(String amountPBF) {
        this.amountPBF = amountPBF;
    }

    public String getNotePbm() {
        return notePbm;
    }

    public void setNotePbm(String notePbm) {
        this.notePbm = notePbm;
    }

    public String getDateTimeEnded() {
        return dateTimeEnded;
    }

    public void setDateTimeEnded(String dateTimeEnded) {
        this.dateTimeEnded = dateTimeEnded;
    }

    public String getLeftDur() {
        return leftDur;
    }

    public void setLeftDur(String leftDur) {
        this.leftDur = leftDur;
    }

    public String getRightDur() {
        return rightDur;
    }

    public void setRightDur(String rightDur) {
        this.rightDur = rightDur;
    }
}
