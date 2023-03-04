package com.example.potholedetectionappv1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class HomeFragmentbackup extends Fragment {
    TextView displayUserEmail;
    Button reportPothole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        displayUserEmail = (TextView) rootView.findViewById(R.id.userEmailLabel);
        reportPothole = (Button) rootView.findViewById(R.id.reportPothole);

        MainActivity item = (MainActivity) getActivity();
        Bundle returnEmail = item.getMyData();
        String userE = returnEmail.getString("userEmail");

        displayUserEmail.setText(userE);
//                this.getArguments().getString("message"));

        reportPothole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> deviceValues = new HashMap<>();

//                deviceValues.put("User's Name", FullName);
//                deviceValues.put("DeviceLongitude", Email);
//                deviceValues.put("DeviceLatitude", Password);
            }
        });


        return rootView;
    }
}