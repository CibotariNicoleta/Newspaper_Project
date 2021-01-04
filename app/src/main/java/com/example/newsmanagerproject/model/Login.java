package com.example.newsmanagerproject.model;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.network.LoginTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    FloatingActionButton login;
    CheckBox remember_me;
    EditText userName, pwd;
    //FirebaseAuth auth;
    TextView go_to_signup, recoverPassword;
    int counter = 3;
    public static boolean isLogged;
    public static boolean ok;
    private ProgressDialog progressDialog;
    Shared shared;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        remember_me = findViewById(R.id.checkBox);
        login = findViewById(R.id.loginButton);
        userName = (EditText) findViewById(R.id.userName);
        pwd = (EditText) findViewById(R.id.pwd);
        recoverPassword = findViewById(R.id.recoverPass);
        shared = new Shared(getApplicationContext());
        go_to_signup = findViewById(R.id.go_to_signup);
        progressDialog = new ProgressDialog(this);

        ok = shared.getSharedPreferences().getBoolean("saveLogin", false);
        if (ok == true) {
            userName.setText(shared.getSharedPreferences().getString("username", ""));
            pwd.setText(shared.getSharedPreferences().getString("password", ""));
            remember_me.setChecked(true);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = userName.getText().toString();
                String Password = pwd.getText().toString();
                if (remember_me.isChecked()) {
                    shared.getEditor().putBoolean("saveLogin", true);
                    shared.getEditor().putString("username", userName.getText().toString());
                    shared.getEditor().putString("password", pwd.getText().toString());
                    shared.getEditor().commit();

                } else {
                    shared.getEditor().clear();
                    shared.getEditor().commit();
                }

                goSignin(user_name, Password);
                if (user_name.equals("DEV_TEAM_09") &&
                        Password.equals("65424")) {

                    MainActivity.loginButton.setVisibility(View.INVISIBLE);
                    LoginTask loginTask= new LoginTask();
                    loginTask.execute();
                    Intent intentGoLogging = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentGoLogging);
                    shared.secondtime();
                } else {
                    counter--;
                    if (counter == 0) {
                        login.setEnabled(false);
                    }
                }
            }
        });



        go_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoSignup = new Intent(getApplicationContext(), Registration.class);
                startActivity(intentGoSignup);
            }
        });

        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
    }

    private  void showRecoverPasswordDialog(){
        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                 //input email
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        } );

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                 //dismiss dialog
                dialog.dismiss();
            }
        } );

        //show dialog
        builder.create().show();

    }

    private void beginRecovery(String email) {
        progressDialog.setMessage("Sending email...");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Email sent", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Login.this, "Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //get and show proper error message
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT);
            }
        });
    }

    protected void goSignin(String email, String password){
          firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if( task.isSuccessful()){
                      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                      if(user.isEmailVerified()){
                          MainActivity.loginButton.setVisibility(View.INVISIBLE);
                          LoginTask loginTask= new LoginTask();
                          loginTask.execute();
                          Intent intentGoLogging = new Intent(getApplicationContext(), MainActivity.class);
                          startActivity(intentGoLogging);
                          shared.secondtime();
                      }else{
                          Toast.makeText(Login.this, "Failed to login! Please, go to register!", Toast.LENGTH_SHORT).show();

                      }

                  }else{
                      Toast.makeText(Login.this, "Failed to login! Please, check your credentials" + task, Toast.LENGTH_SHORT).show();

                  }
              }
          });
    }
}


