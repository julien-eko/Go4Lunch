package com.example.go4lunch.Views;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Models.Details.Details;
import com.example.go4lunch.Models.Restaurant;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.example.go4lunch.Utils.PlaceStream;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class PlaceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_restaurant_item_name)
    TextView name;
    @BindView(R.id.fragment_restaurant_item_address)
    TextView address;
    @BindView(R.id.fragment_restaurant_open_hours)
    TextView openHours;
    @BindView(R.id.fragment_restaurant_item_stars_1)
    ImageView stars1;
    @BindView(R.id.fragment_restaurant_item_stars_2)
    ImageView stars2;
    @BindView(R.id.fragment_restaurant_item_stars_3)
    ImageView stars3;
    @BindView(R.id.fragment_restaurant_item_distance)
    TextView distance;
    @BindView(R.id.fragment_restaurant_item_image)
    ImageView image;
    @BindView(R.id.fragment_restaurant_item_workmates_number)
    TextView workmate;
    private String api_key;
    private static Disposable disposable;
    private static Context context;

    public PlaceViewHolder(View itemView,Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.api_key = BuildConfig.ApiKey;
        this.context=context;
    }

    //update view
    public void updateWithTimesUser(Restaurant restaurant, RequestManager glide) {

        this.name.setText(restaurant.getNameRestaurant());
        this.address.setText(restaurant.getAddress());

        //schedule of restaurant
        executeHttpRequestWithRetrofit(restaurant.getId(),restaurant,openHours);

        this.distance.setText(restaurant.getDistance());

        //search workmate lunch his restaurant
        workmateNumber(restaurant.getId(),workmate);


        if (restaurant.getStars()==0) {
            stars1.setVisibility(View.INVISIBLE);
            stars2.setVisibility(View.INVISIBLE);
            stars3.setVisibility(View.INVISIBLE);
        }
        if (restaurant.getStars()==1){
            stars1.setVisibility(View.INVISIBLE);
            stars2.setVisibility(View.INVISIBLE);
        }
        if (restaurant.getStars()==2){
            stars1.setVisibility(View.INVISIBLE);
        }

        //image restaurant if exist else return default image
        if (restaurant.getImage() != null) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + restaurant.getImage() + "&key=" + api_key;
            glide.load(photoUrl).into(image);
        }else{
            glide.load(R.drawable.restaurant).into(image);
        }

    }

    private void workmateNumber(String restaurantId,TextView workmateNumber){
        UserHelper.getUsersInterestedByRestaurant(restaurantId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                Calendar calendar = Calendar.getInstance();
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                int workmate = 0;
                for (int i = 0; i < querySnapshot.size(); i++) {

                    if (querySnapshot.getDocuments().get(i).get("date").toString().equals(Integer.toString(dayOfYear))) {
                        if (!querySnapshot.getDocuments().get(i).get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            workmate = workmate +1;

                        }

                    }
                }

                workmateNumber.setText("(" + Integer.toString(workmate) + ")");

            }
        });
    }

    //http request for openingHour
    private static void executeHttpRequestWithRetrofit(String placeId, Restaurant restaurant,TextView openHours) {
        disposable = PlaceStream.streamDetails(placeId).subscribeWith(new DisposableObserver<Details>() {
            @Override
            public void onNext(Details details) {

                update(details, restaurant);
            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {
                openHours.setText(restaurant.getSchedule());
            }
        });
    }

    //formated oppeningHours
    public static void update(Details details, Restaurant restaurant) {

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY ));
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));

        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        Integer time = Integer.parseInt(hour + minute);

        Calendar.getInstance().getTime();
        if (details.getResult().getOpeningHours() != null) {
            if (details.getResult().getOpeningHours().getOpenNow()) {
                if (details.getResult().getOpeningHours().getPeriods().size() > 1) {
                    for (int i = 0; i <= dayOfWeek; i++) {
                        if (details.getResult().getOpeningHours().getPeriods().size() > i) {
                            if (Integer.parseInt(details.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime()) > time) {
                                Integer a = (Integer.parseInt(details.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime())) - time;

                                if (((Integer.parseInt(details.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime())) - time) < 70) {

                                    restaurant.setSchedule(context.getResources().getString(R.string.closing_soon));
                                } else {
                                    restaurant.setSchedule(context.getResources().getString(R.string.open_until) + convertDate(details.getResult().getOpeningHours().getPeriods().get(i).getClose().getTime(), Locale.getDefault().getDisplayLanguage()));
                                }

                            }


                        }else{
                            restaurant.setSchedule(context.getResources().getString(R.string.open));
                        }
                    }
                } else {
                    restaurant.setSchedule(context.getResources().getString(R.string.open));
                }
            } else {
                restaurant.setSchedule(context.getResources().getString(R.string.close));
            }
        } else {

                restaurant.setSchedule(context.getResources().getString(R.string.no_information));

        }


    }

    //convert date french and english supportted
    public static String convertDate(String date,String language) {
        int hour = Integer.parseInt(date.substring(0, 2));
        String minute = date.substring(2);

        if( language.equals("English")){
            if (hour > 12) {
                return (hour - 12) + "." + minute + "pm";
            } else if (hour == 12) {
                return "12" + "." + minute + "pm";
            } else if (hour == 0) {
                return "12" + "." + minute + "am";
            } else {
                return hour + "." + minute + "am";
            }
        }else{
            return hour + "h" +minute;
        }



    }
}
