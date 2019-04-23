package com.example.go4lunch.Models;

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

    public static void ListNearbySearch(List<Restaurant> listRestaurant, NearbySearch nearbySearch){
        for(Result result : nearbySearch.getResults()){
            Restaurant restaurant = new Restaurant();

            //set name of restaurant
            restaurant.setNameRestaurant(result.getName());

            restaurant.setAddress(result.getVicinity());


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

            executeHttpRequestWithRetrofit(result.getPlaceId());



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
