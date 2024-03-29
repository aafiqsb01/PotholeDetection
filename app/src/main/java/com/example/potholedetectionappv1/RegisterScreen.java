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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullName, Email, Password;

                FullName = String.valueOf(userFullName.getText());
                Email = String.valueOf(userEmail.getText());
                Password = String.valueOf(userPassword.getText());

                if (FullName.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(RegisterScreen.this, "User details are not filled in properly.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> userValues = new HashMap<>();

                userValues.put("Full Name", FullName);
                userValues.put("Email", Email);
                userValues.put("Password", Password);
                System.out.println(Email);

                database.collection("Users").document(Email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                Toast.makeText(RegisterScreen.this, "The email already possesses an account.", Toast.LENGTH_SHORT).show();
                            }

                            else{
                                if (containsSpecialCharacters(Password) && containsNumber(Password)) {
                                    database.collection("Users").document(Email).set(userValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(RegisterScreen.this, "Success", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterScreen.this, "fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                else {
                                    Toast.makeText(RegisterScreen.this, "The password does not contain any special characters or numbers.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
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

    public static boolean containsNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsSpecialCharacters(String str) {
        String specialCharacters = "[!@#$%£^&*()_+=\\[\\]{};':\"\\\\|,.<>\\/?]";
        Pattern pattern = Pattern.compile(specialCharacters);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}