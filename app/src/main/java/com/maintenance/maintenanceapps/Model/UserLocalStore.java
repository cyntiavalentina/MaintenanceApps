package com.maintenance.maintenanceapps.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {
    SharedPreferences userLocalDatabase;
    Context context;
    SharedPreferences.Editor editor;
    public static final String USER_DETAIL = "userDetail";
    private static final String IS_LOGIN = "is_login";

    private static final String USER_NAME = "user_name";
    private static final String MSG_LOGIN = "msgLogin";
    private static final String VALID_YN = "validYN";
    private static final String USER_ID = "userID";
    private static final String ROLE_ID = "roleID";

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(USER_DETAIL, 0);
        this.context = context;
        editor = userLocalDatabase.edit();
    }

    public void storeUserData(User user) {
        editor.putString(VALID_YN, user.validYN);
        editor.putInt(USER_ID, user.userID);
        editor.putString(MSG_LOGIN, user.msgLogin);
        //editor.putInt(ROLE_ID, user.roleID);
        editor.commit();
    }


    public User getLoggedInUser() {
        String validYN = userLocalDatabase.getString(VALID_YN, "");
        int userID = userLocalDatabase.getInt(USER_ID,0);
        String msgLogin = userLocalDatabase.getString(MSG_LOGIN,"");
       // int roleID = userLocalDatabase.getInt(ROLE_ID, 0);

        User storedUser = new User(validYN,userID,msgLogin);
        return storedUser;

    }

    public void setUserLoggedIn(Boolean loggedIn) {
        editor.putBoolean(IS_LOGIN, loggedIn);
        editor.commit();
    }

    public boolean getUserLoggedIn() {
        return userLocalDatabase.getBoolean(IS_LOGIN, false);
    }

    public void clearUserData() {
        editor.clear();
        editor.commit();
    }
}
