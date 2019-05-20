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
    private  static String phone;
    private static Context mContext;
    private static int workmate;

    public static void ListNearbySearch(List<Restaurant> listRestaurant, NearbySearch nearbySearch,double longitude,double latitude){
        for(Result result : nearbySearch.getResults()){
            Restaurant restaurant = new Restaurant();

            restaurant.setId(result.getPlaceId());

            //set name of restaurant
            restaurant.setNameRestaurant(result.getName());

            //adress
            restaurant.setAddress(result.getVicinity());

            //distance
            float[] distance = new float[1];
            Location.distanceBetween(latitude,longitude,result.getGeometry().getLocation().getLat(),result.getGeometry().getLocation().getLng(),distance);
            restaurant.setDistance(Integer.toString((int)distance[0])+"m");

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
            executeHttpRequestWithRetrofit(result.getPlaceId(),restaurant,result);
            //set number of stars (0..3)
            if(result.getRating()!=null){
                Double rating =result.getRating();
                restaurant.setStars(rating(rating));
            }else{
                restaurant.setStars(0);
            }



            if(result.getPhotos() != null){
                if(result.getPhotos().get(0).getPhotoReference() != null){
                    restaurant.setImage(result.getPhotos().get(0).getPhotoReference());
                }else {
                    restaurant.setImage(null);
                }
            }else{
                restaurant.setImage(null);
            }
            workmateNumber(result.getPlaceId(),restaurant);
            //Log.i("workmate", Integer.toString(workmate));
            //restaurant.setWorksmates(workmate);
            //executeHttpRequestWithRetrofit(result.getPlaceId());



            listRestaurant.add(restaurant);
        }
    }

    private static void executeHttpRequestWithRetrofit(String placeId,Restaurant restaurant,Result result) {
        disposable = PlaceStream.streamDetails(placeId).subscribeWith(new DisposableObserver<Details>() {
            @Override
            public void onNext(Details details) {
                update(details,restaurant,result);
            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        });
    }


    public static void update(Details details,Restaurant restaurant,Result result){

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-2;
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));

        if (minute.length()==1){
            minute = "0" + minute;
        }
        Integer time = Integer.parseInt(hour + minute);

        Calendar.getInstance().getTime();
        if (details.getResult().getOpeningHours() != null) {
            if(details.getResult().getOpeningHours().getOpenNow()){
                if(details.getResult().getOpeningHours().getPeriods().size() !=0){
                for (int i=0;i<=dayOfWeek;i++){
                    if(Integer.parseInt(details.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime()) > time){
                        if((Integer.parseInt(details.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime()) - time  ) < 30  ){
                            restaurant.setSchedule("Closing Soon");
                        }else{
                            restaurant.setSchedule("Open until " + convertDate(details.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime()));
                        }

                    }
                }}else {
                    restaurant.setSchedule("Open");
                }
            }else{
                restaurant.setSchedule("Close");
            }
        }else{
            if (result.getOpeningHours() != null) {
                if (result.getOpeningHours().getOpenNow()){
                    restaurant.setSchedule("Open");
                } else {
                    restaurant.setSchedule("Close");
                }
            }else{
                restaurant.setSchedule("No information");
            }
        }


    }

    public static String convertDate(String date){
        int hour = Integer.parseInt(date.substring(0,2));
        String minute =date.substring(2);

        if(hour > 12){
            return (hour -12) + "." + minute + "pm";
        }
        else if (hour == 12){
            return "12" + "." + minute + "pm";
        }else if (hour == 0){
            return "12" + "." + minute + "am";
        }else{
            return hour + "." + minute + "am";
        }


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

    private static void workmateNumber(String restaurantId,Restaurant restaurant){
        UserHelper.getUsersInterestedByRestaurant(restaurantId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                Calendar calendar = Calendar.getInstance();
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                workmate = 0;
                for (int i = 0; i < querySnapshot.size(); i++) {

                    if (querySnapshot.getDocuments().get(i).get("date").toString().equals(Integer.toString(dayOfYear))) {
                        if (!querySnapshot.getDocuments().get(i).get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            workmate = workmate +1;

                        }

                    }
                }

                restaurant.setWorksmates(workmate);
                Log.i("workmate1", restaurant.getNameRestaurant());
                Log.i("workmate1", Integer.toString(restaurant.getWorksmates()));

            }
        });
    }
}
