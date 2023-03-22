package com.example.potholedetectionappv1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class LogInScreen extends AppCompatActivity implements LocationListener {

    EditText userEmail, userPassword;
    Button loginUser;
    TextView gotoForgotPasswordScreen, gotoRegisterScreen;
    FirebaseFirestore database;

    LocationManager locationManager;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    double Longitude;
    double Latitude;

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

//        Get Location Access
        runLocationManager();

        gotoRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
                startActivity(intent);
                finish();

            }
        });

        gotoForgotPasswordScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordScreen.class);
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

                database.collection("Users").document(Email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
//                                System.out.println(doc.getData().toString());
                                String userPW = doc.getString("Password");
                                String userFullName = doc.getString("Full Name");

                                if (Password.equals(userPW)) {

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("userEmail", Email);
                                    intent.putExtra("userFullName", userFullName);
                                    intent.putExtra("userPassword", userPW);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LogInScreen.this, "Incorrect Password.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                Toast.makeText(LogInScreen.this, "Incorrect email address.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });
            }
        });
    }

    public void runLocationManager() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }
}