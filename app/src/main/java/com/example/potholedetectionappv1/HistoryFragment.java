package com.example.potholedetectionappv1;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {
    FirebaseFirestore database;
    private MainActivity item;
    List<PotholeClass> potholeList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        database = FirebaseFirestore.getInstance();

        recyclerView = rootView.findViewById(R.id.recyclerView);
        potholeList = new ArrayList<>();

        item = (MainActivity) getActivity();
        Bundle returnUserValues = item.getMyData();
        String userFN = returnUserValues.getString("userFullName");

        database.collection("PotholeReports")
                .orderBy("Date and Time", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (userFN.equals(document.getString("Full Name"))) {
                                    Timestamp DandT = document.getTimestamp("Date and Time");
                                    String lat = document.getString("Latitude");
                                    String longi = document.getString("Longitude");
                                    String time = document.getString("Severity");

                                    Date date = DandT.toDate();

                                    int year = date.getYear() + 1900;
                                    int month = date.getMonth() + 1;
                                    int day = date.getDate();

                                    int hours = date.getHours();
                                    int minutes = date.getMinutes();
                                    int seconds = date.getSeconds();

                                    String formattedDate = day + "/" + month + "/" + year;
                                    String formattedTime = hours + ":" + minutes + ":" + seconds;
//
//                                    System.out.println("Date: " + formattedDate);
//                                    System.out.println("Time: " + formattedTime);

                                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                    String addressCombined = null;
                                    try {
                                        System.out.println(lat);
                                        System.out.println(longi);

                                        List<Address> address = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(longi), 1);

                                        addressCombined = address.get(0).getFeatureName() + ", " + address.get(0).getThoroughfare() + ", " + address.get(0).getPostalCode();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    potholeList.add(new PotholeClass("Date: " + formattedDate, "Time: " + formattedTime, addressCombined, "Severity: " + time));

                                }
                            }

                            ReportsAdapter reportsAdapter = new ReportsAdapter(potholeList);
                            recyclerView.setAdapter(reportsAdapter);
                            recyclerView.setHasFixedSize(true);
                        } else {
                            Toast.makeText(getContext(), "Error in retrieving pothole records.", Toast.LENGTH_SHORT).show();
                        }

                    }

                });

        return rootView;
    }
}