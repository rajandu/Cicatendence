package com.cicattendance;

public class studentModel {

    String student_name, student_uid;

    public studentModel(){
    }
    public studentModel(String name, String student_uid) {
        this.student_name = name;
        this.student_uid = student_uid;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_uid() {
        return student_uid;
    }

    public void setStudent_uid(String student_uid) {
        this.student_uid = student_uid;
    }

    @Override
    public String toString() {
        return student_name;
    }




}
