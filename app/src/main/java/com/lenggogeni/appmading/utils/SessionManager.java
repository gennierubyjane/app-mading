package com.lenggogeni.appmading.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.lenggogeni.appmading.activity.LoginActivity;
import com.lenggogeni.appmading.activity.MainActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String USERNAME = "USERNAME";
    public static final String ID = "ID";
    public static final String LEVEL = "LEVEL";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String username, String id, String level){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(USERNAME, username);
        editor.putString(ID, id);
        editor.putString(LEVEL, level);
        editor.apply();

    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MainActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(LEVEL, sharedPreferences.getString(LEVEL, null));

        return user;
    }

    public void logout(){

        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((MainActivity) context).finish();

    }

}
