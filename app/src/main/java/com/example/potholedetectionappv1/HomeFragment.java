package com.example.potholedetectionappv1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements LocationListener {
    TextView displayUserEmail;
    Button reportPothole;
    MainActivity item;
    Spinner spinnerSeverity;

    FirebaseFirestore database;
    LocationManager locationManager;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private String Longitude;
    private String Latitude;
    private ImageButton activateAutomaticDetection;
    private boolean paused;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseFirestore.getInstance();

        displayUserEmail = (TextView) rootView.findViewById(R.id.userEmailLabel);
        reportPothole = (Button) rootView.findViewById(R.id.reportPothole);
        activateAutomaticDetection = (ImageButton) rootView.findViewById(R.id.automaticDetection);

        activateAutomaticDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int icon;

                if (paused) {
                    paused = false;
                    icon = R.drawable.baseline_gps_off_24;
                } else {
                    paused = true;
                    icon = R.drawable.baseline_gps_fixed_24;
                }

                activateAutomaticDetection.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
            }
        });

        spinnerSeverity = (Spinner) rootView.findViewById(R.id.dropdown_Severity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Severities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSeverity.setAdapter(adapter);

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
                String selectedSeverity = spinnerSeverity.getSelectedItem().toString();

//                Date date = new Date();
//                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
//                SimpleDateFormat timeFormatter = new SimpleDateFormat("ss:mm:HH");
//
//                String currentDate = dateFormatter.format(date);
//                String currentTime = dateFormatter.format(timeFormatter);
//
//                System.out.println(dateFormatter.format(date));
//                System.out.println(dateFormatter.format(timeFormatter));

                Date updatedDate = new Date();

                System.out.println("Date and time: " + updatedDate);

                reportValues.put("Full Name", userFN);
                reportValues.put("Date and Time", updatedDate);
                reportValues.put("Latitude", Latitude);
                reportValues.put("Longitude", Longitude);
                reportValues.put("Severity", selectedSeverity);

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

    public void formatTimestamp(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());

        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);

        System.out.println("Date: " + formattedDate);
        System.out.println("Time: " + formattedTime);
    }

    public void alertUserOfUpcomingPothole() {

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