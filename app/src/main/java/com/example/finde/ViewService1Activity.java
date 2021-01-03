package com.example.finde;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewService1Activity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ListView listView;
    private static CustomListAdapter adapter;
    ArrayList<ServiceItem> items;
    List<PlacesPOJO.CustomA> results;
    ApiInterface apiService;
    String latLongString;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_service1);

        Bundle extras = getIntent().getExtras();
        latLongString = extras.getString("LatLong");

        // Set the action bar details
        getSupportActionBar().setTitle("Hospital Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#C7004A")));


        listView = findViewById(R.id.listService1);
        apiService = APIClient.getClient().create(ApiInterface.class);

        progressBar = findViewById(R.id.progressBarHospital);
        progressBar.setVisibility(View.VISIBLE);

        // Add placeholder data
        items = new ArrayList<>();
        //items.add(new ServiceItem("Place 1", "~1.2km far", "1234567890", "12.34, 45.34"));
        //items.add(new ServiceItem("Place 2", "~500m far", "1234567890", "12.34, 45.34"));
        //items.add(new ServiceItem("Place 3", "~2.4km far", "1234567890", "12.34, 45.34"));

        adapter = new CustomListAdapter(items, getApplicationContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ListViewService", "Item selected");
                ServiceItem serviceItem = (ServiceItem) adapterView.getItemAtPosition(i);
                zoomMapToPoint(serviceItem.latLong, serviceItem.getMarker());
            }
        });

        //ServiceAsyncTask serviceAsyncTask = new ServiceAsyncTask(latLongString,
        //        findViewById(android.R.id.content).getRootView(), ViewService1Activity.this);
        //ServiceAsyncTask serviceAsyncTask = new ServiceAsyncTask(latLongString, adapter);
        //serviceAsyncTask.execute("hospital");
        fetchPlaces("hospital");
    }

    private void setUpMaps() {
        // Google Map Integration
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.hospitalMap);
        mapFragment.getMapAsync(this);
    }

    private void drawPlaces() {
        for(ServiceItem serviceItem:items) {

            String[] latLongArr = serviceItem.latLong.split(",");

            // Add a marker in Current Location and move the camera
            LatLng placeLocation = new LatLng(Double.valueOf(latLongArr[0]), Double.valueOf(latLongArr[1]));

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(placeLocation)
                    .title(serviceItem.name));
            serviceItem.setMarker(marker);
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    private void zoomMapToPoint(String coordinates, Marker marker) {
        String[] latLongArr = coordinates.split(",");
        LatLng placeLocation = new LatLng(Double.valueOf(latLongArr[0]), Double.valueOf(latLongArr[1]));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 17));
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());

        marker.showInfoWindow();
    }

    private void fetchPlaces(String placeType) {
        Call<PlacesPOJO.Root> call = apiService.doPlaces(placeType, latLongString,
                "distance", placeType,APIClient.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<PlacesPOJO.Root>() {
            @Override
            public void onResponse(Call<PlacesPOJO.Root> call, Response<PlacesPOJO.Root> response) {
                PlacesPOJO.Root root = response.body();

                if( response.isSuccessful() ) {
                    if( root.status.equals("OK") ) {
                        results = root.customA;

                        for(int i=0;i<results.size();i++) {
                            if( i==3 ) {
                                break;
                            }

                            PlacesPOJO.CustomA info = results.get(i);
                            fetchDistance(info);
                        }
                        setUpMaps();

                    }
                }
            }

            @Override
            public void onFailure(Call<PlacesPOJO.Root> call, Throwable t) {
                call.cancel();
                Toast.makeText(ViewService1Activity.this, "Unable to make request", Toast.LENGTH_SHORT);
            }
        });
    }

    private void fetchDistance(final PlacesPOJO.CustomA info) {
        Call<ResultDistanceMatrix> call = apiService.getDistance(APIClient.GOOGLE_PLACE_API_KEY,
                latLongString, info.geometry.locationA.lat+","+info.geometry.locationA.lng);

        call.enqueue(new Callback<ResultDistanceMatrix>() {
            @Override
            public void onResponse(Call<ResultDistanceMatrix> call, Response<ResultDistanceMatrix> response) {
                ResultDistanceMatrix resultDistanceMatrix = response.body();
                if( "OK".equalsIgnoreCase(resultDistanceMatrix.status) ) {
                    ResultDistanceMatrix.InfoDistanceMatrix infoDistanceMatrix =
                            resultDistanceMatrix.rows.get(0);
                    ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement distanceElement =
                            (ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement)
                                    infoDistanceMatrix.elements.get(0);
                    if( "OK".equalsIgnoreCase(distanceElement.status) ) {
                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDuration =
                                distanceElement.duration;
                        ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDistance =
                                distanceElement.distance;
                        String totalDistance = String.valueOf(itemDistance.text);
                        String totalDuration = String.valueOf(itemDuration.text);

                        items.add(new ServiceItem(info.name, totalDistance, "1234567890",
                                info.geometry.locationA.lat+","+info.geometry.locationA.lng));

                        if( items.size()==3||items.size()==results.size() ) {
                            //adapter = new CustomListAdapter(items, getApplicationContext());
                            //listView.setAdapter(adapter);
                            adapter.updateServiceItems(items);
                            adapter.notifyDataSetChanged();
                            drawPlaces();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultDistanceMatrix> call, Throwable t) {
                call.cancel();
            }
        });
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

        String[] latLongArr = latLongString.split(",");

        // Add a marker in Current Location and move the camera
        LatLng currentLocation = new LatLng(Double.valueOf(latLongArr[0]), Double.valueOf(latLongArr[1]));
        mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Your Location"))
                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((currentLocation), 16));
    }
}
