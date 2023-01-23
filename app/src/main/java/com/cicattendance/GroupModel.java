package com.cicattendance;

import com.google.firebase.auth.FirebaseUser;

public class GroupModel {

    public GroupModel(){}

    String groupName;


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupModel(String groupName){
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return groupName;
    }


}
