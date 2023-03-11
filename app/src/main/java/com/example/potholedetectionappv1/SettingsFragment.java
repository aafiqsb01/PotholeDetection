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
    FirebaseFirestore database;

    TextView logOutLabel;
    private MainActivity item;
    private TextView label_e;
    private TextView label_pw;
    private TextView label_fn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        database = FirebaseFirestore.getInstance();

        label_fn = rootView.findViewById(R.id.user_FullName);
        label_e = rootView.findViewById(R.id.user_Email);
        label_pw = rootView.findViewById(R.id.user_Password);

        item = (MainActivity) getActivity(); 
        Bundle returnUserValues = item.getMyData();
        String userE = returnUserValues.getString("userEmail");
        String userFN = returnUserValues.getString("userFullName");
        String userPW = returnUserValues.getString("userPassword");

        label_fn.setText(userFN);
        label_e.setText(userE);
        label_pw.setText(userPW);

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