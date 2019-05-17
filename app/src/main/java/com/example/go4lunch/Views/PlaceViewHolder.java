package com.example.go4lunch.Views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Models.Restaurant;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public PlaceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.api_key = BuildConfig.ApiKey;
    }

    //update view
    public void updateWithTimesUser(Restaurant restaurant, RequestManager glide) {


        this.name.setText(restaurant.getNameRestaurant());
        this.address.setText(restaurant.getAddress());
        this.openHours.setText(restaurant.getSchedule());
        this.distance.setText(restaurant.getDistance());
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
}
