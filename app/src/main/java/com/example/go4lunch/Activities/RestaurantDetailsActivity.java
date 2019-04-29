package com.example.go4lunch.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Models.Details.Details;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.PlaceStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class RestaurantDetailsActivity extends AppCompatActivity {

    @BindView(R.id.activity_restaurant_adress)
    TextView adress;
    @BindView(R.id.activity_restaurant_item_stars_1)
    ImageView stars1;
    @BindView(R.id.activity_restaurant_item_stars_2)
    ImageView stars2;
    @BindView(R.id.activity_restaurant_item_stars_3)
    ImageView stars3;
    @BindView(R.id.activity_restaurant_name)
    TextView nameRestaurant;
    @BindView(R.id.activity_restaurant_image)
    ImageView image;
    private Disposable disposable;
    private String placeId;
    private String photo;
    private String api_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);
        placeId = getIntent().getStringExtra("restaurant");
        photo = getIntent().getStringExtra("photo");
        this.api_key= BuildConfig.ApiKey;
        this.executeHttpRequestWithRetrofit();
    }



    private void executeHttpRequestWithRetrofit() {
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


    public void update(Details details){
        this.adress.setText(details.getResult().getFormattedAddress());
        this.nameRestaurant.setText(details.getResult().getName());

        //rating
        Double rating =details.getResult().getRating();
        Integer stars = rating(rating);

        if (stars==0) {
            stars1.setVisibility(View.INVISIBLE);
            stars2.setVisibility(View.INVISIBLE);
            stars3.setVisibility(View.INVISIBLE);
        }
        if (stars==1){
            stars1.setVisibility(View.INVISIBLE);
            stars2.setVisibility(View.INVISIBLE);
        }
        if (stars==2){
            stars1.setVisibility(View.INVISIBLE);
        }

        //photo
        if (photo != null) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo + "&key=" + api_key;
            Glide.with(this).load(photoUrl).into(image);
        }else{
            Glide.with(this).load(R.drawable.restaurant).into(image);
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
}
