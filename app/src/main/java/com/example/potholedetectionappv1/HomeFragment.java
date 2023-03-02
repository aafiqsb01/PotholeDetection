package com.example.potholedetectionappv1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    TextView displayUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        displayUserEmail = (TextView) rootView.findViewById(R.id.userEmailLabel);
        displayUserEmail.setText(this.getArguments().getString("message"));

        return rootView;
    }
}