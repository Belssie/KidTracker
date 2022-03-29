package com.example.kidtracker.main.Data;

public class FeedingData {
    String feedingDateTime;
    String feedingType;
    String breastStarted;

    public FeedingData(String feedingDateTime, String feedingType){
        this.feedingDateTime = feedingDateTime;
        this.feedingType = feedingType;
    }
    public FeedingData(String feedingDateTime, String feedingType, String breastStarted) {
        this.feedingDateTime = feedingDateTime;
        this.feedingType = feedingType;
        this.breastStarted = breastStarted;
    }

    public FeedingData(){}

    @Override
    public String toString() {
        return "FeedingData{" +
                "feedingDateTime='" + feedingDateTime + '\'' +
                ", feedingType='" + feedingType + '\'' +
                ", breastStarted='" + breastStarted + '\'' +
                '}';
    }

    public String getFeedingDateTime() {
        return feedingDateTime;
    }

    public void setFeedingDateTime(String feedingDateTime) {
        this.feedingDateTime = feedingDateTime;
    }

    public String getFeedingType() {
        return feedingType;
    }

    public void setFeedingType(String feedingType) {
        this.feedingType = feedingType;
    }

    public String getBreastStarted() {
        return breastStarted;
    }

    public void setBreastStarted(String breastStarted) {
        this.breastStarted = breastStarted;
    }
}
