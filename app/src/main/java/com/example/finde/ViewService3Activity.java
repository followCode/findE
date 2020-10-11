package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

public class ViewService3Activity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ListView listView;
    private static CustomListAdapter adapter;
    ArrayList<ServiceItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_service3);

        // Set the action bar details
        getSupportActionBar().setTitle("Police Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.policeMap);
        mapFragment.getMapAsync(this);

        listView = findViewById(R.id.listService3);

        // Add placeholder data
        items = new ArrayList<>();
        items.add(new ServiceItem("Place 1", "~1.2km far", "1234567890", "12.34, 45.34"));
        items.add(new ServiceItem("Place 2", "~500m far", "1234567890", "12.34, 45.34"));
        items.add(new ServiceItem("Place 3", "~2.4km far", "1234567890", "12.34, 45.34"));

        adapter = new CustomListAdapter(items, getApplicationContext());
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(12.9716, 77.5946);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Place 1"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.9725, 77.5965))
                .title("Place 2"));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(12.9737, 77.5937))
                .title("Place 3"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((sydney), 15));
    }
}
