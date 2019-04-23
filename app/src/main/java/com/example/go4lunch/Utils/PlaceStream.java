package com.example.go4lunch.Utils;

import com.example.go4lunch.Models.Details.Details;
import com.example.go4lunch.Models.Search.NearbySearch;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlaceStream {

    public static Observable<NearbySearch> streamNearbySearch(String location) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);
        return placeService.getNearbySearch(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<Details> streamDetails(String placeId) {
        PlaceService placeService = PlaceService.retrofit.create(PlaceService.class);
        return placeService.getDetail(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}
