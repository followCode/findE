package com.example.finde;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("place/nearbysearch/json?")
    Call<PlacesPOJO.Root> doPlaces(@Query(value = "type", encoded = true) String type,
                                   @Query(value = "location", encoded = true) String location,
                                   @Query(value = "rankby", encoded = true) String rankby,
                                   @Query(value = "keyword", encoded = true) String keyword,
                                   @Query(value = "key", encoded = true) String key);


    @GET("distancematrix/json") // origins/destinations:  LatLng as string
    Call<ResultDistanceMatrix> getDistance(@Query("key") String key, @Query("origins") String origins,
                                           @Query("destinations") String destinations);
}