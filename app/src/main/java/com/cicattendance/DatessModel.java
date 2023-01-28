package com.cicattendance;

public class DatessModel {

    public DatessModel(String date) {
        this.date = date;
    }

    String date ;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public DatessModel(){

    }

    @Override
    public String toString() {
        return date;
    }




}
