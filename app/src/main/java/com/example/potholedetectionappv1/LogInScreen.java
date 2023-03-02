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

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

public class LogInScreen extends AppCompatActivity {

    EditText userEmail, userPassword;
    Button loginUser;
    TextView gotoForgotPasswordScreen, gotoRegisterScreen;
//    FirebaseAuth mAuth;

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

        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        loginUser = findViewById(R.id.login);
        gotoForgotPasswordScreen = findViewById(R.id.forgotPassword);
        gotoRegisterScreen = findViewById(R.id.register);

//        mAuth = FirebaseAuth.getInstance();

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email, Password;

                Email = String.valueOf(userEmail.getText());
                Password = String.valueOf(userPassword.getText());

//                if (FullName.isEmpty()){
//                    Toast.makeText(RegisterScreen.this, "Please enter your full name.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (Email.isEmpty()){
                    Toast.makeText(LogInScreen.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Password.isEmpty()){
                    Toast.makeText(LogInScreen.this, "Please enter your password.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                mAuth.signInWithEmailAndPassword(Email, Password)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Toast.makeText(LogInScreen.this, "Log In Successful.",
//                                            Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    intent.putExtra("message",Email);
//                                    startActivity(intent);
//                                    finish();
//
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    System.out.println("Failed");
//                                    Toast.makeText(LogInScreen.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
            }
        });
        gotoRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
                startActivity(intent);
                finish();

            }
        });
    }
}