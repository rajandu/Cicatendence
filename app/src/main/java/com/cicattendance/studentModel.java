package com.cicattendance;

public class studentModel {


    public studentModel(){
    }
    public studentModel(String name) {
        this.student_name = name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    @Override
    public String toString() {
        return student_name;
    }
    String student_name;

}
