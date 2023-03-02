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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterScreen extends AppCompatActivity {

    EditText userFullName, userEmail, userPassword;
    Button registerUser, loginUserInstead;
//    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

//        mAuth = FirebaseAuth.getInstance();

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

//                if (FullName.isEmpty()){
//                    Toast.makeText(RegisterScreen.this, "Please enter your full name.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (Email.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Password.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                mAuth.createUserWithEmailAndPassword(Email, Password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                progressBar.setVisibility(View.GONE);
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(RegisterScreen.this, "Account registered.",
//                                            Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getApplicationContext(), LogInScreen.class);
//                                    startActivity(intent);
//                                    finish();
//
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(RegisterScreen.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });
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