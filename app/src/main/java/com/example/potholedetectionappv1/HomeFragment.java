package com.example.potholedetectionappv1;

import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements LocationListener {
    TextView displayUserEmail;
    Button reportPothole;
    MainActivity item;

    FirebaseFirestore database;
    LocationManager locationManager;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private String Longitude;
    private String Latitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseFirestore.getInstance();

        displayUserEmail = (TextView) rootView.findViewById(R.id.userEmailLabel);
        reportPothole = (Button) rootView.findViewById(R.id.reportPothole);

//        Get Location Access
        runLocationManager();

        item = (MainActivity) getActivity();
        Bundle returnEmail = item.getMyData();
        String userE = returnEmail.getString("userEmail");
        String userFN = returnEmail.getString("userFullName");

        displayUserEmail.setText(userE);

        reportPothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> reportValues = new HashMap<>();
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String currentDate = new SimpleDateFormat("dd.MM.YYYY", Locale.getDefault()).format(new Date());

                reportValues.put("Full Name", userFN);
                reportValues.put("Date", currentTime);
                reportValues.put("Time", currentTime);
                reportValues.put("Latitude", Latitude);
                reportValues.put("Longitude", Longitude);

                database.collection("PotholeReports").add(reportValues).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
//                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return rootView;
    }

    public void runLocationManager () {
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitude = String.valueOf(location.getLatitude());
        Longitude = String.valueOf(location.getLongitude());
    }
}