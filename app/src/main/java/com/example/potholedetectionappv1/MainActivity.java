package com.example.potholedetectionappv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        navBar.setSelectedItemId(R.id.nav_home);

        String userE = getIntent().getStringExtra("message");
        Toast.makeText(MainActivity.this, userE, Toast.LENGTH_SHORT).show();

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:
//                        Bundle additionalValues = new Bundle();
//                        additionalValues.putString("message", userE);
                        fragment = new HomeFragment();
//                        fragment.setArguments(additionalValues);
                        break;

                    case R.id.nav_history:
                        fragment = new HistoryFragment();
                        break;

                    case R.id.nav_map:
                        fragment = new MapsFragment();
                        break;

                    case R.id.nav_settings:
                        fragment = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                return true;
            }
        });
    }
}