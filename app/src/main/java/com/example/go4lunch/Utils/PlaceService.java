package com.example.go4lunch.Utils;

import com.example.go4lunch.Models.Search.NearbySearch;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceService {

    String apiKey="AIzaSyDyz6E5UGMEIrZeQHw2pyYox5MFO59DSOE";

    //api Place Search
    @GET("nearbysearch/json?radius=1500&type=restaurant&key="+apiKey)
    Observable<NearbySearch> getNearbySearch(@Query("location") String location );


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}

