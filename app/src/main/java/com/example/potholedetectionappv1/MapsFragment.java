package com.example.potholedetectionappv1;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Use the googleMap object to customize the map
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                LatLng location = new LatLng(51.563794, -0.365930); // Create a LatLng object with the location coordinates
                String title = "Pothole 1"; // Specify the title for the marker
                String snippet = "small"; // Specify the snippet for the marker

                LatLng location1 = new LatLng(51.577557, -0.368588); // Create a LatLng object with the location coordinates
                String title1 = "Pothole 2"; // Specify the title for the marker
                String snippet1 = "small"; // Specify the snippet for the marker

                LatLng location2 = new LatLng(51.592857, -0.349542); // Create a LatLng object with the location coordinates
                String title2 = "Pothole 3"; // Specify the title for the marker
                String snippet2 = "small"; // Specify the snippet for the marker

                LatLng location3 = new LatLng(51.592840, -0.350746); // Create a LatLng object with the location coordinates
                String title3 = "Pothole 4"; // Specify the title for the marker
                String snippet3 = "medium"; // Specify the snippet for the marker

                googleMap.addMarker(new MarkerOptions().position(location).title(title).snippet(snippet)); // Add the marker to the map
                googleMap.addMarker(new MarkerOptions().position(location1).title(title1).snippet(snippet2));
                googleMap.addMarker(new MarkerOptions().position(location2).title(title2).snippet(snippet2));
                googleMap.addMarker(new MarkerOptions().position(location3).title(title3).snippet(snippet3));
            }
        });


        return rootView;
    }
}