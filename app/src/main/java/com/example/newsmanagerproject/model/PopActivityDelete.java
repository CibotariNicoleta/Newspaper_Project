package com.example.newsmanagerproject.model;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.newsmanagerproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PopActivityDelete extends AppCompatActivity {

    FloatingActionButton close;
    FloatingActionButton accept;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_delete);
        close = findViewById(R.id.closeButton);
        accept = findViewById(R.id.acceptButton);
        dialog = new Dialog(this);

        //if you close then you go to the main screen

    }

}
