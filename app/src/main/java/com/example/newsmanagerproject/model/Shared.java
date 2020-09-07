package com.example.newsmanagerproject.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared {
    //to create sharedpreferences file
    SharedPreferences sharedPreferences;
    //to edit the sharedpreferences file
    SharedPreferences.Editor editor;
    //context pass the references to another class
    Context context;

    //When you click in login set true
    public static boolean checkLogin;

    int mode = 0;

    //shared preferences name
    String fileName = "loginPrefer";
    //store the boolean value with respect to key id
    String Data = "b";

    public Shared(Context context)
    {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(fileName, mode );
        editor = sharedPreferences.edit();
    }


    //for second time user
    public void secondtime(){
        checkLogin = true;
        editor.putBoolean(Data, true);
        editor.commit();
    }

    //for first time user
    public void firstTime(){
        if(!this.login()){
            checkLogin = false;
        } else checkLogin = true;
    }

    public boolean login(){
        return sharedPreferences.getBoolean(Data, false);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }
}
