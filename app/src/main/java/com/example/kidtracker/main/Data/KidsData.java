package com.example.kidtracker.main.Data;
public class KidsData {
    private String kidId;
    private String name, gender;
    private String picture;
    private String birthDate;

    public KidsData(String kidId, String name, String gender, String picture, String birthDate) {
        this.kidId = kidId;
        this.name = name;
        this.gender = gender;
        this.picture = picture;
        this.birthDate = birthDate;
    }

    public KidsData(){}
    @Override
    public String toString() {
        return "KidsData{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", picture=" + picture +
                ", birthDate=" + birthDate +
                '}';
    }

    public String getId() { return kidId; }

    public void setId(String id) { this.kidId = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
