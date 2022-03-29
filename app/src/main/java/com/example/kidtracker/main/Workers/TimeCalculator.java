package com.example.kidtracker.main.Workers;

import android.annotation.SuppressLint;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeCalculator {
    Date start_date, end_date;
    String startDate, endDate;

    public TimeCalculator(Date start_date, Date end_date) {
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public TimeCalculator(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String findDifference(Date start_date, Date end_date) {

        String seconds, minutes, hours, days, years;

        long difference_In_Time = start_date.getTime() - end_date.getTime();

        long difference_In_Seconds = (difference_In_Time / 1000) % 60;

        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

        long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

        long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

        long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

        if (difference_In_Seconds>0){
            seconds = difference_In_Seconds + "s ";
        } else {
            seconds = "";
        }

        if (difference_In_Minutes>0){
            minutes = difference_In_Minutes +"m ";
        } else {
            minutes = "";
        }

        if (difference_In_Hours>0){
            hours = difference_In_Hours +"h ";
        } else {
            hours = "";
        }

        if (difference_In_Years>0){
            years = difference_In_Years +"Y ";
        } else {
            years = "";
        }

        if (difference_In_Days>0){
            days = difference_In_Days +"D ";
        } else {
            days = "";
        }


        return years + days + hours + minutes + seconds;
    }

    public String findDifference(String startDate, String endDate) throws ParseException {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        Date d1 = sdf.parse(startDate);
        Date d2 = sdf.parse(endDate);
        assert d1 != null;
        assert d2 != null;

        String seconds, minutes, hours, days, years;

        long difference_In_Time = d2.getTime() - d1.getTime();

        long difference_In_Seconds = (difference_In_Time / 1000) % 60;

        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

        long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

        long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

        long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

        if (difference_In_Seconds>0){
            seconds = difference_In_Seconds + "s ";
        } else {
            seconds = "";
        }

        if (difference_In_Minutes>0){
            minutes = difference_In_Minutes +"m ";
        } else {
            minutes = "";
        }

        if (difference_In_Hours>0){
            hours = difference_In_Hours +"h ";
        } else {
            hours = "";
        }

        if (difference_In_Years>0){
            years = difference_In_Years +"Y ";
        } else {
            years = "";
        }

        if (difference_In_Days>0){
            days = difference_In_Days +"d ";
        } else {
            days = "";
        }

        return years + days + hours + minutes + seconds;
    }

    public String isDateBefore(String startDate, String endDate) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm");
        Date d1 = sdf.parse(startDate);
        Date d2 = sdf.parse(endDate);
        assert d1 != null;
        assert d2 != null;

        String isItBefore;

        if (d1.after(d2)){
            isItBefore = "no";
        } else {
            isItBefore = "yes";
        }
        return isItBefore;
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String age(String startDate, String endDate) throws ParseException {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdf.parse(startDate);
        Date d2 = sdf.parse(endDate);
        assert d1 != null;
        assert d2 != null;

        String days, years, months;
        String age = "";


        long difference_In_Time = d2.getTime() - d1.getTime();

        long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

        long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

        long difference_In_Months = (difference_In_Days / 30);


        if (difference_In_Years>0){
            if (difference_In_Years == 1){
                years = ", " + difference_In_Years +" year old";
            } else {
                years = ", " + difference_In_Years + " years old";
            }
            age += years;
        } else if (difference_In_Months>0) {
            if (difference_In_Months == 1){
                months = ", " + difference_In_Months + " month old";
            } else {
                months = ", " + difference_In_Months + " months old";
            }
            age += months;
        } else if (difference_In_Days>0) {
            if (difference_In_Days == 1){
                days = ", " + difference_In_Days + " day old";
            } else {
                days = ", " + difference_In_Days + " days old";
            }
            age += days;
        } else {
            age += ", a few hours old";
        }

        return age;
    }
}
