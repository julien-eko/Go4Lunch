package com.example.go4lunch.Views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Models.Firestore.Restaurants;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.Models.Restaurant;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.RestaurantsHelper;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.fragment_workmates_image)
    ImageView image;
    @BindView(R.id.fragment_workmates_text)
    TextView textView;
    private Boolean isActivityRestaurant;
    private Context context;


    public WorkmatesViewHolder(Context context, View itemView, Boolean isActivityRestaurant) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = context;
        this.isActivityRestaurant=isActivityRestaurant;
    }

    //update view
    public void updateUser(User user, RequestManager glide) {

            Calendar calendar = Calendar.getInstance();
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

            if(isActivityRestaurant == false) {
                if (user.getDate() == dayOfYear && user.getRestaurantChoiceId() != null) {
                    String text = user.getUsername() + context.getString(R.string.is_eating_at) + user.getRestaurantName();
                    textView.setText(text);
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                } else {
                    String text2 = user.getUsername() + context.getString(R.string.hasnt_decided_yet);
                    textView.setText(text2);
                    textView.setTextColor(context.getResources().getColor(R.color.grey));
                }
                if (user.getUrlPicture() != null) {
                    glide.load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(image);
                }
            }else{
                if (user.getDate() == dayOfYear) {
                    String text = user.getUsername() + context.getResources().getString(R.string.is_joining);
                    textView.setText(text);
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                    if (user.getUrlPicture() != null) {
                        glide.load(user.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(image);
                    }
                }
            }




    }
}
