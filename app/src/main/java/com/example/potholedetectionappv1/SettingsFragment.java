package com.example.potholedetectionappv1;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {
    Button LogUserOut;
    FirebaseAuth auth;
    FirebaseUser user;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        auth = FirebaseAuth.getInstance();
        LogUserOut = rootView.findViewById(R.id.logout);
//        user = auth.getCurrentUser();
//        if (user == null) {
//            Intent intent = new Intent(getApplicationContext(), LogInScreen.class);
//            startActivity(intent);
//            finish();
//        }

//        LogUserOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//
//                intent = new Intent(getApplicationContext(), LogInScreen.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        return rootView;
    }
}