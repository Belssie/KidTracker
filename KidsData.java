package com.example.kidtracker;

import android.graphics.Bitmap;

import java.util.Arrays;
import java.util.Date;

public class KidsData {
    private String name, gender;
    private int age;
    private byte[] picture;
    private int birthDate;

    public KidsData(String name, String gender, int age, byte[] picture, int birthDate) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.picture = picture;
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "KidsData{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", picture=" + Arrays.toString(picture) +
                ", birthDate=" + birthDate +
                '}';
    }

    public KidsData(){}

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        this.birthDate = birthDate;
    }
}
