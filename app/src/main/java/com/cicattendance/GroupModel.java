package com.cicattendance;

import com.google.firebase.auth.FirebaseUser;

public class GroupModel {

    String group_name;
    public GroupModel(){}

    public String getgroup_name() {
        return group_name;
    }

    public void setgroup_name(String group_name) {
        this.group_name = group_name;
    }


    public GroupModel(String group_name){
        this.group_name = group_name;
    }
}
