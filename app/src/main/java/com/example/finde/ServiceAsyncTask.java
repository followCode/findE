package com.example.finde;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.finde.APIClient.GOOGLE_PLACE_API_KEY;

public class ServiceAsyncTask extends AsyncTask<String, Void, ArrayList<ServiceItem>> {

    ApiInterface apiService;
    String latLongString;
    View serviceView;
    AppCompatActivity serviceActivity;
    List<PlacesPOJO.CustomA> results;
    ArrayList<ServiceItem> items;

    private CustomListAdapter customListAdapter;

    ServiceAsyncTask(String coordinates, CustomListAdapter adapter) {
        latLongString = coordinates;
        /*serviceView = rootView;
        serviceActivity = activity;*/
        customListAdapter = adapter;
        apiService = APIClient.getClient().create(ApiInterface.class);

        items = new ArrayList<>();
    }

    protected ArrayList<ServiceItem> doInBackground(String... params) {

        fetchPlaces(params[0]);

        return items;
    }

    private void fetchPlaces(String placeType) {
        Call<PlacesPOJO.Root> call = apiService.doPlaces(placeType, latLongString,
                "distance", placeType,GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<PlacesPOJO.Root>() {
            @Override
            public void onResponse(Call<PlacesPOJO.Root> call, Response<PlacesPOJO.Root> response) {
                PlacesPOJO.Root root = response.body();

                if( response.isSuccessful() ) {
                    if( root.status.equals("OK") ) {
                        results = root.customA;

                        for(int i=0;i<results.size();i++) {
                            if( i==3 )
                                break;

                            PlacesPOJO.CustomA info = results.get(i);
                            fetchDistance(info);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PlacesPOJO.Root> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void fetchDistance(final PlacesPOJO.CustomA info) {
        Call<ResultDistanceMatrix> call = apiService.getDistance(GOOGLE_PLACE_API_KEY,
                latLongString, info.geometry.locationA.lat+","+info.geometry.locationA.lng);

        call.enqueue(new Callback<ResultDistanceMatrix>() {
            @Override
            public void onResponse(Call<ResultDistanceMatrix> call, Response<ResultDistanceMatrix> response) {

                ResultDistanceMatrix resultDistanceMatrix = response.body();
                if( "OK".equalsIgnoreCase(resultDistanceMatrix.status) ) {

                    ResultDistanceMatrix.InfoDistanceMatrix infoDistanceMatrix =
                            resultDistanceMatrix.rows.get(0);
                    ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement distanceElement =
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
                            return;
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
    protected  void onPostExecute(ArrayList<ServiceItem> resp) {
        /*CustomListAdapter adapter = new CustomListAdapter(resp, serviceActivity.getApplicationContext());
        final ListView listView = (ListView)serviceView.findViewById(R.id.listService1);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

        Log.d("Async Collect: ", "Size of collected data: "+resp.size());

        customListAdapter.updateServiceItems(items);
        customListAdapter.notifyDataSetChanged();

    }
}
