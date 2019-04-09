package com.maintenance.maintenanceapps.Model;

import java.security.PublicKey;

public class User {
    public String nip;
    public String password;
    public String validYN;
     public int userID;
    public String msgLogin;
    public int appsid;
    //public int roleID;


    public User(int userID) {
        this.userID = userID;
    }


    public User(String nip, String password, int appsid){
        this.nip = nip;
        this.password = password;
        this.appsid = appsid;
        //this.roleID = roleID;

    }

    public User(String validYN, int userID, String msgLogin){
        this.validYN = validYN;
        this.userID = userID;
        this.msgLogin = msgLogin;
        //this.roleID = roleID;
    }

    public User(String nip) {this.nip = nip; }

    public int getUserID() {
        return userID;
    }

    //public int getRoleID(){return  roleID;}

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getNip() {
        return nip;
    }
}
