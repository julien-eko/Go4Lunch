package com.example.go4lunch.Models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.go4lunch.Models.Details.Details;
import com.example.go4lunch.Models.Search.NearbySearch;
import com.example.go4lunch.Models.Search.Result;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.example.go4lunch.Utils.PlaceStream;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListRestaurant {

    private static Disposable disposable;
    private static String phone;
    private static Context mContext;
    private static int workmate;

    public static void ListNearbySearch(List<Restaurant> listRestaurant, NearbySearch nearbySearch, double longitude, double latitude) {
        for (Result result : nearbySearch.getResults()) {

            Restaurant restaurant = new Restaurant();


            //executeHttpRequestWithRetrofit(result.getPlaceId(), restaurant, result);


            restaurant.setId(result.getPlaceId());

            //set name of restaurant
            restaurant.setNameRestaurant(result.getName());

            //adress
            restaurant.setAddress(result.getVicinity());

            //distance
            float[] distance = new float[1];
            Location.distanceBetween(latitude, longitude, result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng(), distance);
            restaurant.setDistance(Integer.toString((int) distance[0]) + "m");

            //open or close
            /*
            if (result.getOpeningHours() != null) {
                if (result.getOpeningHours().getOpenNow()){
                    restaurant.setSchedule("open");
                } else {
                    restaurant.setSchedule("close");
                }
            }else{
                restaurant.setSchedule("no information");
            }
*/

            //set number of stars (0..3)
            if (result.getRating() != null) {
                Double rating = result.getRating();
                restaurant.setStars(rating(rating));
            } else {
                restaurant.setStars(0);
            }


            if (result.getPhotos() != null) {
                if (result.getPhotos().get(0).getPhotoReference() != null) {
                    restaurant.setImage(result.getPhotos().get(0).getPhotoReference());
                } else {
                    restaurant.setImage(null);
                }
            } else {
                restaurant.setImage(null);
            }
            //workmateNumber(result.getPlaceId(), restaurant);
            //Log.i("workmate", Integer.toString(workmate));
            //restaurant.setWorksmates(workmate);
            //executeHttpRequestWithRetrofit(result.getPlaceId());


            listRestaurant.add(restaurant);
        }
    }



    public static Integer rating(double rating) {
        rating = (rating / 5) * 3;

        if (rating < 0.75)
            return 0;
        if (rating >= 0.75 && rating < 1.5)
            return 1;
        if (rating >= 1.5 && rating < 2.25)
            return 2;
        else
            return 3;

    }


}
