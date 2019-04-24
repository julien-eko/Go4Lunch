package com.example.go4lunch.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.example.go4lunch.Models.Details.Details;
import com.example.go4lunch.Models.Search.NearbySearch;
import com.example.go4lunch.Models.Search.Result;
import com.example.go4lunch.Utils.PlaceStream;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListRestaurant {

    private static Disposable disposable;
    private  static String phone;
    private static Context mContext;

    public static void ListNearbySearch(List<Restaurant> listRestaurant, NearbySearch nearbySearch,double longitude,double latitude){
        for(Result result : nearbySearch.getResults()){
            Restaurant restaurant = new Restaurant();

            //set name of restaurant
            restaurant.setNameRestaurant(result.getName());

            //adress
            restaurant.setAddress(result.getVicinity());

            //distance
            float[] distance = new float[1];
            Location.distanceBetween(latitude,longitude,result.getGeometry().getLocation().getLat(),result.getGeometry().getLocation().getLng(),distance);
            restaurant.setDistance(Integer.toString((int)distance[0])+"m");

            //open or close
            if (result.getOpeningHours() != null) {
                if (result.getOpeningHours().getOpenNow()){
                    restaurant.setSchedule("open");
                } else {
                    restaurant.setSchedule("close");
                }
            }else{
                restaurant.setSchedule("no information");
            }

            //set number of stars (0..3)
            Double rating =result.getRating();
            restaurant.setStars(rating(rating));


            if(result.getPhotos() != null){
                if(result.getPhotos().get(0).getPhotoReference() != null){
                    restaurant.setImage(result.getPhotos().get(0).getPhotoReference());
                }else {
                    restaurant.setImage(null);
                }
            }else{
                restaurant.setImage(null);
            }


            //executeHttpRequestWithRetrofit(result.getPlaceId());



            listRestaurant.add(restaurant);
        }
    }

    private static void executeHttpRequestWithRetrofit(String placeId) {
        disposable = PlaceStream.streamDetails(placeId).subscribeWith(new DisposableObserver<Details>() {
            @Override
            public void onNext(Details details) {
                update(details);
            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        });
    }


    public static void update(Details details){
        phone=details.getResult().getFormattedPhoneNumber();

    }


    public static Integer rating (double rating){
        rating = (rating/5)*3;

        if(rating < 0.75 )
            return 0;
        if (rating >= 0.75 && rating < 1.5)
            return 1;
        if (rating >= 1.5 && rating < 2.25)
            return 2;
        else
            return 3;

    }
}
