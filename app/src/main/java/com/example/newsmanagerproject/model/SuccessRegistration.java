package com.example.newsmanagerproject.model;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsmanagerproject.R;

public class SuccessRegistration extends AppCompatActivity {

    private Button goHomePage;
    private Button goSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_registration);

        goHomePage = findViewById(R.id.goHomePage);
        goSignIn = findViewById(R.id.goSignin);

        goHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessRegistration.this, MainActivity.class);
                startActivity(intent);
            }
        });

        goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessRegistration.this, Login.class);
              //  intent.putExtra("sendNewUser", getIntent().getSerializableExtra("newUser"));
                startActivity(intent);
            }
        });
    }
}
