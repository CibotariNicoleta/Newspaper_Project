package com.example.newsmanagerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newsmanagerproject.model.MainActivity;
import com.example.newsmanagerproject.model.Shared;
import com.example.newsmanagerproject.network.LoginTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Login extends AppCompatActivity {

    FloatingActionButton login;
    CheckBox remember_me;
    EditText userName, pwd;
    int counter = 3;
    public static boolean isLogged;
    public static boolean ok;
    Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        remember_me = findViewById(R.id.checkBox);
        login = findViewById(R.id.loginButton);
        userName = (EditText) findViewById(R.id.userName);
        pwd = (EditText) findViewById(R.id.pwd);
        shared = new Shared(getApplicationContext());

        ok = shared.getSharedPreferences().getBoolean("saveLogin", false);
        if (ok == true) {
            userName.setText(shared.getSharedPreferences().getString("username", ""));
            pwd.setText(shared.getSharedPreferences().getString("password", ""));
            remember_me.setChecked(true);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (remember_me.isChecked()) {
                    shared.getEditor().putBoolean("saveLogin", true);
                    shared.getEditor().putString("username", userName.getText().toString());
                    shared.getEditor().putString("password", pwd.getText().toString());
                    shared.getEditor().commit();

                } else {
                    shared.getEditor().clear();
                    shared.getEditor().commit();
                }

                if (userName.getText().toString().equals("DEV_TEAM_09") &&
                        pwd.getText().toString().equals("65424")) {

                    MainActivity.loginButton.setVisibility(View.INVISIBLE);
                    LoginTask loginTask= new LoginTask();
                    loginTask.execute();
                    Intent intentGoLogging = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentGoLogging);
                    shared.secondtime();


                } else {
                    Toast.makeText(getApplicationContext(), "Wrong" +
                            "Credentials", Toast.LENGTH_SHORT).show();
                    counter--;
                    if (counter == 0) {
                        login.setEnabled(false);
                    }
                }
            }
        });
    }
}


