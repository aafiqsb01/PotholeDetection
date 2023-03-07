package com.example.potholedetectionappv1;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {
    Button LogUserOut;
    FirebaseFirestore database;

    TextView logOutLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        database = FirebaseFirestore.getInstance();

        logOutLabel = rootView.findViewById(R.id.logout);
        logOutLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogInScreen.class);
                startActivity(intent);

            }
        });

        return rootView;
    }
}