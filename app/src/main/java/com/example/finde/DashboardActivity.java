package com.example.finde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView navButton;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Button hospitalButton;
    private Button ambulanceButton;
    private Button policeButton;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().hide();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        drawerLayout = findViewById(R.id.dash_drawer);
        navigationView = findViewById(R.id.dash_nav);
        navigationView.setItemIconTintList(null);
        hospitalButton = findViewById(R.id.buttonService1);
        ambulanceButton = findViewById(R.id.buttonService2);
        policeButton = findViewById(R.id.buttonService3);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(Gravity.LEFT);
                //Toast.makeText(DashboardActivity.this, "Clicked: "+(id), Toast.LENGTH_SHORT).show();
                switch(id){
                    case R.id.nav_item_one:
                        // Handle Edit Profile
                        Intent profile = new Intent(DashboardActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        break;
                    case R.id.nav_item_two:
                        // Handle Change Password
                        Intent passw = new Intent(DashboardActivity.this, EnterOldPasswordActivity.class);
                        startActivity(passw);
                        break;
                    case R.id.nav_item_three:
                        // Handle Sign Out
                        Intent signOut = new Intent(DashboardActivity.this, SignOutActivity.class);
                        startActivity(signOut);
                        finish();
                        break;
                }

                return false;
            }
        });

        navButton = findViewById(R.id.dashboardNavButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT, true);
            }
        });


        /*
        *  Start of handling button clicks
        * */

        hospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ViewService1Activity.class);
                startActivity(i);
            }
        });

        ambulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ViewService2Activity.class);
                startActivity(i);
            }
        });

        policeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ViewService3Activity.class);
                startActivity(i);
            }
        });

        /*
        *  End of handling button clicks
        * */

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(12.9716, 77.5946);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((sydney), 15));
    }

}