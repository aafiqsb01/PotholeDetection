package com.example.potholedetectionappv1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (userFN.equals(document.getString("Full Name"))) {
                                    String d = document.getString("Date");
                                    String t = document.getString("Time");
                                    String lat = document.getString("Latitude");
                                    String longi = document.getString("Longitude");
                                    String time = document.getString("Severity");

                                    potholeList.add(new PotholeClass("Date: " + d, "Time: " + t, "Latitude: " + lat, "Longitude: " + longi, "Severity: " + time));

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