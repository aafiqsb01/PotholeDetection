package com.example.potholedetectionappv1;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment implements SensorEventListener, LocationListener {
    TextView displayUserEmail;
    Button reportPothole;
    MainActivity item;
    Spinner spinnerSeverity;
    FirebaseFirestore database;
    LocationManager locationManager;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private double Longitude;
    private double Latitude;
    private ImageButton activateAutomaticDetection;
    private boolean paused;
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private static SensorManager SensorManager;

    private AlertDialog currentDialog = null;
    private ArrayList<String> alreadyAlerted;

    private static Sensor CompassSensor;
    private TextView detectionStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseFirestore.getInstance();

        displayUserEmail = (TextView) rootView.findViewById(R.id.userEmailLabel);
        reportPothole = (Button) rootView.findViewById(R.id.reportPothole);
        activateAutomaticDetection = (ImageButton) rootView.findViewById(R.id.automaticDetection);
        detectionStatus = (TextView) rootView.findViewById(R.id.DetectionStatusLabel);
        detectionStatus.setText("Detection Inactive");

//        Defining Accelerometer Sensor variables
        SensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);;
        CompassSensor = SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        activateAutomaticDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int icon;

                if (paused) {
                    paused = false;
                    icon = R.drawable.baseline_gps_off_24;
                    detectionStatus.setText("Detection Inactive");
                } else {
                    paused = true;
                    icon = R.drawable.baseline_gps_fixed_24;
                    detectionStatus.setText("Detection Active");
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

        alreadyAlerted = new ArrayList<>();
        alertUserOfUpcomingPothole.run();
        reportPothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> reportValues = new HashMap<>();
                Date updatedDate = new Date();
                String selectedSeverity = spinnerSeverity.getSelectedItem().toString();

                reportValues.put("Full Name", userFN);
                reportValues.put("Date and Time", updatedDate);
                reportValues.put("Latitude", Latitude);
                reportValues.put("Longitude", Longitude);
                reportValues.put("Severity", selectedSeverity);

                database.collection("PotholeReports").add(reportValues).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Pothole reported.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Fail.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return rootView;
    }

    private final Runnable testalertUserOfUpcomingPothole = new Runnable() {
        @Override
        public void run() {
            double lat = 51.592106;
            double longi = -0.361616;

//            System.out.println(Latitude);
//            System.out.println(Longitude);

            if ( distance(Latitude, Longitude, lat , longi) ) {
                displayNotification();

            }
            mHandler.postDelayed(this, 100);
        }
    };

    private final Runnable alertUserOfUpcomingPothole = new Runnable() {
        @Override
        public void run() {

            database.collection("PotholeReports")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    double lat = Double.parseDouble(String.valueOf(document.get("Latitude")));
                                    double longi = Double.parseDouble(String.valueOf(document.get("Longitude")));

                                    if (distance(Latitude, Longitude, lat , longi)) {
                                        if (alreadyAlerted.contains(document.getId())) {
                                            return;
                                        }
                                        else{
                                            alreadyAlerted.add(document.getId());
                                            displayNotification();
                                        }
                                    }
                                }
                            }
                        }
                    });
            mHandler.postDelayed(this, 100);
        }
    };

    /** calculates the distance between two locations in MILES */
    private boolean distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6.975e+6; // in yards, miles = 3958.75, change to 6371 for kilometers

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);

        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double dist = earthRadius * c;

        if (dist <= 100) {
            return true;
        }
        return false;
    }

    private void displayNotification (){
        if (currentDialog != null && currentDialog.isShowing()) {
            // A dialog is already showing, do not show another one
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert");
        builder.setMessage("Upcoming pothole.");
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        AlertDialog dialog = builder.create();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // Perform any necessary actions when the dialog is canceled
                currentDialog = null;
            }
        });
        currentDialog = dialog;
        dialog.show();
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
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}