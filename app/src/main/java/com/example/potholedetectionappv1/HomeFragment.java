package com.example.potholedetectionappv1;

import static android.content.Context.SENSOR_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements LocationListener, SensorEventListener {
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

    private Handler mHandler = new Handler();
    private Runnable runnable;
    static SensorManager SensorManager1;
    static Sensor CompassSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseFirestore.getInstance();

        displayUserEmail = (TextView) rootView.findViewById(R.id.userEmailLabel);
        reportPothole = (Button) rootView.findViewById(R.id.reportPothole);
        activateAutomaticDetection = (ImageButton) rootView.findViewById(R.id.automaticDetection);

//
////        Defining Accelerometer Sensor variables
//        SensorManager1 = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);;
//        CompassSensor = SensorManager1.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        SensorManager1.registerListener((SensorEventListener) getActivity(), CompassSensor, SensorManager.SENSOR_DELAY_NORMAL);

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

//        Severity Drop Down Menu
        spinnerSeverity = (Spinner) rootView.findViewById(R.id.dropdown_Severity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Severities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerSeverity.setAdapter(adapter);

//        Get Location Access
        runLocationManager();

//        Getting user email and full name from main activity
        item = (MainActivity) getActivity();
        Bundle returnEmail = item.getMyData();
        String userE = returnEmail.getString("userEmail");
        String userFN = returnEmail.getString("userFullName");

        displayUserEmail.setText(userE);

//        alertUserOfUpcomingPothole.run();

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

                displayNotification();

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

//    private final Runnable alertUserOfUpcomingPothole = new Runnable() {
//        @Override
//        public void run() {
//            //get user location
//            //get pothole data
//            database.collection("PotholeReports")
//                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                            if (error != null) {
//                                Toast.makeText(getContext(), "Database error.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            for (DocumentChange document : value.getDocumentChanges()) {
//                                if (document.getType() == DocumentChange.Type.ADDED) {
//                                    double lat = Double.parseDouble(document.getDocument().getString("Latitude"));
//                                    double longi = Double.parseDouble(document.getDocument().getString("Longitude"));
//
//                                    if ( (latitude_isWithinRange(Double.parseDouble(Latitude), lat)) && (longitude_isWithinRange(Double.parseDouble(Longitude), longi) )) {
//                                        System.out.println(Latitude + " is within 10% of " + lat);
//
////                                        displayNotification();
//
//                                    } else {
//                                        Toast.makeText(getContext(), "Pothole upcoming.", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                    }
//
//                            }
//                        }
//                    });
//
//            mHandler.postDelayed(this, 100);
//            //chuck range compare for example 10% out.
//            //if user is near a pothole
//            //pop up notification for alert
//
//        }
//    };

    public static boolean latitude_isWithinRange(double num1, double num2) {
        double lowerBound = num2 * 0.99999;
        double upperBound = num2 * 1.00001;
        return num1 >= lowerBound && num1 <= upperBound;
    }

    public static boolean longitude_isWithinRange(double num1, double num2) {
        double lowerBound = num2 * 0.99868;
        double upperBound = num2 * 1.00132;
        return num1 >= lowerBound && num1 <= upperBound;
    }

    private void displayNotification (){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pothole Alert")
                .setMessage("Upcoming Pothole")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
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

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}