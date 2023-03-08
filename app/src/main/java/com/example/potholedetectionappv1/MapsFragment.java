package com.example.potholedetectionappv1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MapsFragment extends Fragment {

    FirebaseFirestore database;
    ArrayList<String> Title;
    ArrayList<String> Longitude;
    ArrayList<String> Latitude;
    ArrayList<String> Severity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        database = FirebaseFirestore.getInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                database.collection("PotholeReports")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 1;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                System.out.println(doc.getData().toString());
                                        Double lat = Double.parseDouble(document.getString("Latitude"));
                                        Double longi = Double.parseDouble(document.getString("Longitude"));
                                        String snippet = document.getString("Severity");
                                        LatLng location = new LatLng(lat, longi);

                                        googleMap.addMarker(new MarkerOptions().position(location).title("Pothole" + Integer.toString(count)).snippet(snippet));
                                        count += 1;
                                    }
                                }
                                else {
                                    Toast.makeText(getContext(), "Incorrect email address.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            }
                        });
            }
        });

        return rootView;
    }
}