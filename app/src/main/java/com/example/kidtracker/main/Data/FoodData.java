package com.example.kidtracker.main.Data;

public class FoodData {
    String dateTimeFood;
    String amountFood;
    String typeFood;
    String noteFood;

    public FoodData(String dateTimeFood, String amountFood, String typeFood, String noteFood) {
        this.dateTimeFood = dateTimeFood;
        this.amountFood = amountFood;
        this.typeFood = typeFood;
        this.noteFood = noteFood;
    }

    public FoodData(){}

    @Override
    public String toString() {
        return "FoodData{" +
                "dateTimeFood='" + dateTimeFood + '\'' +
                ", amountFood='" + amountFood + '\'' +
                ", typeFood='" + typeFood + '\'' +
                ", noteFood='" + noteFood + '\'' +
                '}';
    }

    public String getDateTimeFood() {
        return dateTimeFood;
    }

    public void setDateTimeFood(String dateTimeFood) {
        this.dateTimeFood = dateTimeFood;
    }

    public String getAmountFood() {
        return amountFood;
    }

    public void setAmountFood(String amountFood) {
        this.amountFood = amountFood;
    }

    public String getTypeFood() {
        return typeFood;
    }

    public void setTypeFood(String typeFood) {
        this.typeFood = typeFood;
    }

    public String getNoteFood() {
        return noteFood;
    }

    public void setNoteFood(String noteFood) {
        this.noteFood = noteFood;
    }
}
