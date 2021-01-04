package com.example.newsmanagerproject.model;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.newsmanagerproject.R;
import com.example.newsmanagerproject.database.ArticleDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Registration extends AppCompatActivity {
    private EditText userName;
    private EditText Email;
    private EditText Password;
    private EditText ConfirmPassword;
    private TextView go_Signin;
    private Button signup;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        userName = findViewById(R.id.userNameRegister);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        ConfirmPassword = findViewById(R.id.confirmPassword);
        go_Signin = findViewById(R.id.go_to_signin);
        signup = findViewById(R.id.Signup);




        go_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGoSignip = new Intent(getApplicationContext(), Login.class);
                startActivity(intentGoSignip);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = userName.getText().toString();
                final String email_adress = Email.getText().toString();
                final String password_name = Password.getText().toString();
                final String confirm_passwors = ConfirmPassword.getText().toString();

                if(!password_name.equals(confirm_passwors)){
                  ConfirmPassword.setError("Confirmation password is incorrect");
                  ConfirmPassword.requestFocus();
                }else if(TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email_adress) ||
                        TextUtils.isEmpty(password_name) || TextUtils.isEmpty(confirm_passwors)){

                    Toast.makeText(Registration.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email_adress).matches()){
                    Email.setError("Please enter a valid Email");
                    Email.requestFocus();

                }else if(password_name.length()<6){
                    Password.setError("Password should be greater than 6 characters");
                    Password.requestFocus();
                }else if(password_name.length()<6) {
                    userName.setError("Usarname should be greater than 6 characters");
                    userName.requestFocus();
                }else {
                    Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved1");
                    register(user_name, email_adress, password_name);
                }

            }
        });


    }

    public void register(String user_name, String email_adress, String password_name){
        signup.setEnabled(false);
         firebaseAuth.createUserWithEmailAndPassword(email_adress, password_name).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful())
                 {
                     Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved2");
                     sendVerificationEmail(user_name, email_adress, password_name);
                 }
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 if(e instanceof FirebaseAuthUserCollisionException)//this means email already registered
                 {
                     Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved3");
                     Email.setError("Email Already Registered");
                     Email.requestFocus();
                     signup.setEnabled(true);
                 }
                 else
                 {
                     Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved4");
                     Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved4"+e.getMessage());

                     Toast.makeText(Registration.this, "Opps! Something went wrong", Toast.LENGTH_SHORT).show();
                     signup.setEnabled(true);
                 }
             }
         });
    }


    private void sendVerificationEmail(String user_name, String email, String password){
        if(firebaseAuth.getCurrentUser()!=null)
        {
            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved4");
                        Toast.makeText(Registration.this, "Email has been sent to your  email address", Toast.LENGTH_SHORT ).show();
                        User user = new User(user_name, email, password);
                        ArticleDB.saveNewUser(user);
                        Intent intent = new Intent(Registration.this, SuccessRegistration.class);

                       // intent.putExtra("newUser", user);
                        startActivity(intent);
                        signup.setEnabled(true);
                    }
                    else
                    {
                        Logger.log(Logger.INFO, "uuu" + " --------- >>>>>>>(Article) retrieved5");
                        Toast.makeText(Registration.this, "Opps! Faild to send verification email", Toast.LENGTH_SHORT ).show();
                        signup.setEnabled(true);
                    }
                }
            });


        }
    }
}


