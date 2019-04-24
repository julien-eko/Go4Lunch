package com.example.go4lunch.Views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.Models.Restaurant;
import com.example.go4lunch.R;

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


    public PlaceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //update view
    public void updateWithTimesUser(Restaurant restaurant, RequestManager glide) {


        this.name.setText(restaurant.getNameRestaurant());
        this.address.setText(restaurant.getAddress());
        this.openHours.setText(restaurant.getSchedule());
        this.distance.setText(restaurant.getDistance());

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

    }

}
