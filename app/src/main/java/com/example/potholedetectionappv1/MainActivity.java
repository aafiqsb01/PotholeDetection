package com.example.potholedetectionappv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navBar;
    String userE;
    String userFN;
    String userPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        navBar.setSelectedItemId(R.id.nav_home);

        userE = getIntent().getStringExtra("userEmail");
        userFN = getIntent().getStringExtra("userFullName");
        userPW = getIntent().getStringExtra("userPassword");

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_home:;
                        fragment = new HomeFragment();
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

                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).addToBackStack(null).commit();
                return true;
            }
        });
    }

    public Bundle getMyData() {
        Bundle extra = new Bundle();
        extra.putString("userEmail", userE);
        extra.putString("userFullName", userFN);
        extra.putString("userPassword", userPW);
        return extra;
    }
}