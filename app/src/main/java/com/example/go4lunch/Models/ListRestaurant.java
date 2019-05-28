package com.example.go4lunch.Models;

import android.content.Context;
import android.location.Location;
import com.example.go4lunch.Models.Search.NearbySearch;
import com.example.go4lunch.Models.Search.Result;
import java.util.List;
import io.reactivex.disposables.Disposable;

public class ListRestaurant {

    private static Disposable disposable;
    private static String phone;
    private static Context mContext;
    private static int workmate;

    public static void ListNearbySearch(List<Restaurant> listRestaurant, NearbySearch nearbySearch, double longitude, double latitude) {
        for (Result result : nearbySearch.getResults()) {

            //create new restaurant
            Restaurant restaurant = new Restaurant();

            //add id
            restaurant.setId(result.getPlaceId());

            //add name
            restaurant.setNameRestaurant(result.getName());

            //add adress
            restaurant.setAddress(result.getVicinity());

            //calcul distance between user and restaurant
            float[] distance = new float[1];
            Location.distanceBetween(latitude, longitude, result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng(), distance);
            restaurant.setDistance(Integer.toString((int) distance[0]) + "m");


            //set number of stars (0..3)
            if (result.getRating() != null) {
                Double rating = result.getRating();
                restaurant.setStars(rating(rating));
            } else {
                restaurant.setStars(0);
            }

            //add photo if exist
            if (result.getPhotos() != null) {
                if (result.getPhotos().get(0).getPhotoReference() != null) {
                    restaurant.setImage(result.getPhotos().get(0).getPhotoReference());
                } else {
                    restaurant.setImage(null);
                }
            } else {
                restaurant.setImage(null);
            }

            //add new restaurant at the list
            listRestaurant.add(restaurant);
        }
    }

    //convert rating restaurant
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
