package com.cicattendance;
public class GroupModel {

    public GroupModel(){}

    String group_name;


    public String getgroup_name() {
        return group_name;
    }

    public void setgroup_name(String group_name) {
        this.group_name = group_name;
    }

    public GroupModel(String group_name){
        this.group_name = group_name;
    }

    @Override
    public String toString() {
        return group_name;
    }


}
