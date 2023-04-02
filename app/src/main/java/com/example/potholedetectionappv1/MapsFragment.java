package com.example.potholedetectionappv1;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsFragment extends Fragment implements LocationListener {

    FirebaseFirestore database;
    int count = 1;

    double Latitude, Longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        database = FirebaseFirestore.getInstance();

        ReportsUpdateListener();

        return rootView;
    }

    private void ReportsUpdateListener() {
        if (!isAdded()) {
            // Fragment not attached yet, do nothing
            return;
        }
        database.collection("PotholeReports")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), "Database error.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange document : value.getDocumentChanges()) {
                            if (document.getType() == DocumentChange.Type.ADDED) {
                                double lat = Double.parseDouble(String.valueOf(document.getDocument().get("Latitude")));
                                double longi = Double.parseDouble(String.valueOf(document.getDocument().get("Longitude")));
                                String snippet = document.getDocument().getString("Severity");
                                LatLng location = new LatLng(lat, longi);

                                addLocationsOnMap(location, count, snippet);
                                count += 1;

                            }
                        }
                    }
                });
    }

    public void addLocationsOnMap(LatLng location, int count, String snippet) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                googleMap.addMarker(new MarkerOptions().position(location).title("Pothole" + Integer.toString(count)).snippet(snippet));

            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
    }
}