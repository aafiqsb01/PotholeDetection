package com.example.potholedetectionappv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordScreen extends AppCompatActivity {

    private TextView userEmail, userNewPassword, verifyUserEmail;
    private String uEmail, uPassword;
    public static FirebaseFirestore database;
    private Button resetPassword, gotoLogInScreen, gotoRegisterScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);

        database = FirebaseFirestore.getInstance();

        userEmail = findViewById(R.id.email);
        verifyUserEmail = findViewById(R.id.verifyEmailLabel);
        userNewPassword = findViewById(R.id.newpassword);
        resetPassword = findViewById(R.id.resetPassword);
        gotoLogInScreen = findViewById(R.id.LogIn);
        gotoRegisterScreen = findViewById(R.id.register);

        gotoRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
                startActivity(intent);
                finish();

            }
        });

        gotoLogInScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogInScreen.class);
                startActivity(intent);
                finish();
            }
        });

        verifyUserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uEmail = String.valueOf(userEmail.getText());

                if (!(uEmail.isEmpty())){
                    database.collection("Users").document(uEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc = task.getResult();
                                if (doc.exists()) {
//                                System.out.println(doc.getData().toString());
                                    String userPW = doc.getString("Password");
                                    userNewPassword.setVisibility(View.VISIBLE);
                                    resetPassword.setVisibility(View.VISIBLE);

                                } else {
                                    Toast.makeText(ForgotPasswordScreen.this, "Incorrect email address.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(ForgotPasswordScreen.this, "Enter your email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uPassword = String.valueOf(userNewPassword.getText());

                database.collection("Users").document(uEmail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (!(uPassword.equals(doc.getString("Password")))) {

                                if (containsSpecialCharacters(uPassword) && containsNumber(uPassword)) {
                                    Toast.makeText(ForgotPasswordScreen.this, "The password a special character and number.", Toast.LENGTH_SHORT).show();

                                    writeNewPWtoDatabase(uEmail, uPassword);
                                    userNewPassword.clearComposingText();

                                    Intent intent = new Intent(getApplicationContext(), LogInScreen.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(ForgotPasswordScreen.this, "The password does not contain any special characters or numbers.", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(ForgotPasswordScreen.this, "Use a new password please.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }

    public static void writeNewPWtoDatabase(String user, String pw) {
        database.collection("Users").document(user).update("Password", pw);

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