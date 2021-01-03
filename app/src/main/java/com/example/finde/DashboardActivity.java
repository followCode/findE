package com.example.finde;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView navButton;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Button hospitalButton;
    private Button ambulanceButton;
    private Button policeButton;
    private GoogleMap mMap;
    private TextView username;
    private TextView curLocationTextView;
    String latLong;

    /*Location implementation*/
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().hide();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        drawerLayout = findViewById(R.id.dash_drawer);
        navigationView = findViewById(R.id.dash_nav);
        navigationView.setItemIconTintList(null);
        hospitalButton = findViewById(R.id.buttonService1);
        ambulanceButton = findViewById(R.id.buttonService2);
        policeButton = findViewById(R.id.buttonService3);
        curLocationTextView = findViewById(R.id.CurrentLocation);


        View header = navigationView.getHeaderView(0);
        username = header.findViewById(R.id.drawerUsername);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String displayName = user.getDisplayName();
        username.setText(displayName);


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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(DashboardActivity.this);
                        dialog.setTitle("SIGN OUT")
                                .setCancelable(false)
                                .setMessage("Do you really want to Sign Out?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(getApplicationContext(), "Signed Out successfully", Toast.LENGTH_SHORT).show();
                                        Intent signIn = new Intent(DashboardActivity.this, SignInActivity.class);
                                        signIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(signIn);
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });

                        dialog.create().show();
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
                i.putExtra("LatLong", latLong);
                startActivity(i);
            }
        });

        ambulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ViewService2Activity.class);
                i.putExtra("LatLong", latLong);
                startActivity(i);
            }
        });

        policeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashboardActivity.this, ViewService3Activity.class);
                i.putExtra("LatLong", latLong);
                startActivity(i);
            }
        });

        /*
        *  End of handling button clicks
        * */

    }

    private class AddressAsyncTask extends AsyncTask<Double, String, String> {

        private String resp;

        @Override
        protected String doInBackground(Double... params) {
            publishProgress("Getting address...");

            try {
                List<Address> addresses = geocoder.getFromLocation(params[0], params[1],2);
                // Get the first address line
                String address = addresses.get(0).getAddressLine(0);
                // Update current location text view with the address line
                resp = address;
            } catch(Exception e) {
                resp = "error";
                resp = "R Church Stop, 80 Feet Rd, Binna Mangala, New Tippasandra, Bengaluru, Karnataka 560038, India";
                Log.e("Dash:Location:", e.toString());
            }

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.contains("error")) {
                curLocationTextView.setText(result);
            } else {
                curLocationTextView.setText("Unable to get address");
            }
        }

        @Override
        protected void onProgressUpdate(String... text) {
            curLocationTextView.setText(text[0]);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get status of location permission
        int res = getApplicationContext()
                    .checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);

        // Check if permission for location is granted
        if( res== PackageManager.PERMISSION_GRANTED ) {
            // Get the last recorded location of the user
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if( location!=null ) {
                                // Create a market with the location
                                LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                latLong = String.valueOf(curLocation.latitude) + "," + String.valueOf(curLocation.longitude);
                                mMap.addMarker(new MarkerOptions()
                                        .position(curLocation)
                                        .title("Your location"))
                                        .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                // Animate to zoom to the marker
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((curLocation), 15));

                                /*try {
                                    // Get the addresses available for given location
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                                            location.getLongitude(), 2);
                                    // Get the first address line
                                    String address = addresses.get(0).getAddressLine(0);
                                    // Update current location text view with the address line
                                    curLocationTextView.setText(address);

                                } catch(Exception e) {
                                    // In the case we weren't able to get address
                                    Toast.makeText(getApplicationContext(), "Error: Unable to get address",
                                            Toast.LENGTH_SHORT).show();
                                    Log.e("Dash:Location:", e.toString());
                                }*/
                                AddressAsyncTask runner = new AddressAsyncTask();
                                runner.execute(location.getLatitude(), location.getLongitude());
                            } else {
                                // If location wasn't available
                                Toast.makeText(getApplicationContext(), "Error: Unable to get user current location",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // If the permission wasn't granted
            Toast.makeText(getApplicationContext(), "Error: Permission not given",
                                Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(this);

    }

}