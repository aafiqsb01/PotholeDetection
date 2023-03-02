//package com.example.potholedetectionappv1;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class MainActivity extends AppCompatActivity {
//    Intent intent;
//    BottomNavigationView navBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        navBar = findViewById(R.id.bottomNavigationView);
//        intent = new Intent(getApplicationContext(), HomeScreen.class);
//        startActivity(intent);
//        finish();
//        navBar.setSelectedItemId(R.id.nav_home);
//
//        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        String userE = getIntent().getStringExtra("message");
//                        System.out.println(userE);
//                        intent = new Intent(getApplicationContext(), HomeScreen.class);
//                        startActivity(intent);
//                        intent.putExtra("message", userE);
//                        finish();
//
//                    case R.id.nav_history:
//                        intent = new Intent(getApplicationContext(), HistoryScreen.class);
//                        startActivity(intent);
//                        finish();
//
//                    case R.id.nav_map:
//                        intent = new Intent(getApplicationContext(), MapsScreen.class);
//                        startActivity(intent);
//                        finish();
//
//                    case R.id.nav_settings:
//                        intent = new Intent(getApplicationContext(), SettingsScreen.class);
//                        startActivity(intent);
//                        finish();
//                }
//
//                return true;
//            }
//        });
//    }
//}