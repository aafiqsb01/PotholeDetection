package com.example.potholedetectionappv1;

import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements LocationListener {
    TextView displayUserEmail;
    Button reportPothole;

    static SensorManager SensorManager1;
    static Sensor AccelerometerSensor;
    LocationManager locationManager;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);


//        Defining Accelerometer Sensor variables
        SensorManager1 = (SensorManager) getSystemService(SENSOR_SERVICE);
        AccelerometerSensor = SensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorManager1.registerListener(this, AccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        //        Get Location Access
        runLocationManager();

        displayUserEmail = (TextView) rootView.findViewById(R.id.userEmailLabel);
        reportPothole = (Button) rootView.findViewById(R.id.reportPothole);

        MainActivity item = (MainActivity) getActivity();
        Bundle returnEmail = item.getMyData();
        String userE = returnEmail.getString("userEmail");
        String userFN = returnEmail.getString("userFN");

        displayUserEmail.setText(userE);
//                this.getArguments().getString("message"));

        reportPothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> userValues = new HashMap<>();

//                userValues.put("Full Name", userFN);
//                userValues.put("Email", Email);
//                userValues.put("Password", Password);
            }
        });

        return rootView;
    }

    public void runLocationManager () {
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentSpeed = location.getSpeed() ;
        currentSpeed = currentSpeed * 2.237f;

        DeviceSpeed.setText(String.valueOf("Device speed: " + currentSpeed + " mph"));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[ 0 ];
        y = event.values[ 1 ];
        z = event.values[ 2 ];

        double gforce = Math.sqrt((x * x) + (y * y) + (z * z)) / 9.8;
        GForceValue.setText(String.valueOf(gforce));
    }

    public double calculateGForce (float X_Value, float Y_Value, float Z_Value){
        double GForce_Value = Math.sqrt((X_Value * X_Value) + (Y_Value * Y_Value) + (Z_Value * Z_Value)) / 9.8;
//        System.out.println("G-Force value is:" + GForce_Value);

        return GForce_Value;
    }

    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}