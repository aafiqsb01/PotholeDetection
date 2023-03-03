package com.example.potholedetectionappv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInScreen extends AppCompatActivity {

    EditText userEmail, userPassword;
    Button loginUser;
    TextView gotoForgotPasswordScreen, gotoRegisterScreen;
    FirebaseFirestore database;

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        database = FirebaseFirestore.getInstance();

        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        loginUser = findViewById(R.id.login);
        gotoForgotPasswordScreen = findViewById(R.id.forgotPassword);
        gotoRegisterScreen = findViewById(R.id.register);

        gotoRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
                startActivity(intent);
                finish();

            }
        });

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email, Password;
                Email = String.valueOf(userEmail.getText());
                Password = String.valueOf(userPassword.getText());

                if (Email.isEmpty()) {
                    Toast.makeText(LogInScreen.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Password.isEmpty()) {
                    Toast.makeText(LogInScreen.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                database.collection("users").document(Email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
//                                System.out.println(doc.getData().toString());
                                String userPW = doc.getString("Password");

                                if (Password.equals(userPW)) {

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("message", Email);
                                    startActivity(intent);
                                    finish();
                                }

                                else{
                                    Toast.makeText(LogInScreen.this, "Incorrect Password.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            else {
                                Toast.makeText(LogInScreen.this, "Incorrect email address.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });
            }
        });
    }
}