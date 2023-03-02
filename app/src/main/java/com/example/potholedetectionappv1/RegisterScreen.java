package com.example.potholedetectionappv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterScreen extends AppCompatActivity {

    EditText userFullName, userEmail, userPassword;
    Button registerUser, loginUserInstead;
    FirebaseFirestore database;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        database = FirebaseFirestore.getInstance();

        userFullName = findViewById(R.id.fullname);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        registerUser = findViewById(R.id.register);
        loginUserInstead = findViewById(R.id.loginNow);
        progressBar = findViewById(R.id.progressbar);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String FullName, Email, Password;

                FullName = String.valueOf(userFullName.getText());
                Email = String.valueOf(userEmail.getText());
                Password = String.valueOf(userPassword.getText());

                if (FullName.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "Please enter your full name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Email.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Password.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String,Object> userValues = new HashMap<>();

                userValues.put("Full Name", FullName);
                userValues.put("Email", Email);
                userValues.put("Password", Password);

                database.collection("users").document(Email).set(userValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterScreen.this, "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterScreen.this, "fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loginUserInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LogInScreen.class);
                startActivity(intent);
                finish();

            }
        });
    }
}