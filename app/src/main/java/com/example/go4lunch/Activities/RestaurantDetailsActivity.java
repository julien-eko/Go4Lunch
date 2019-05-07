package com.example.go4lunch.Activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.go4lunch.Base.BaseActivity;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Models.Details.Details;
import com.example.go4lunch.Models.Firestore.Restaurants;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.RestaurantsHelper;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.example.go4lunch.Utils.PlaceStream;
import com.example.go4lunch.Views.WorkmatesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class RestaurantDetailsActivity extends BaseActivity {

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
    @BindView(R.id.activity_restaurant_floating_action_button)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.activity_restaurant_like_button)
    Button likeButton;
    @BindView(R.id.activity_restaurant_details_recycler_view)
    RecyclerView recyclerView;


    private WorkmatesAdapter adapter;
    private Disposable disposable;
    private String restautantId;
    private String restaurantChoice;
    private String photo;
    private String api_key;
    private Boolean like;

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_restaurant_details;
    }

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);
        restautantId = getIntent().getStringExtra("restaurant");
        photo = getIntent().getStringExtra("photo");
        this.api_key = BuildConfig.ApiKey;
        this.configureRecyclerView();
        this.configureRestaurant();
        this.configurelike();

        this.executeHttpRequestWithRetrofit();
    }


    @OnClick(R.id.activity_restaurant_floating_action_button)
    public void OnClickFloatingButton() {

        if (floatingActionButton.isActivated() == false) {
            Drawable drawable = getResources().getDrawable(R.drawable.baseline_done_white_24).mutate();
            floatingActionButton.getBackground().setColorFilter(getResources().getColor(R.color.quantum_lightgreen),
                    PorterDuff.Mode.SRC_ATOP);
            floatingActionButton.setImageDrawable(drawable);
            this.updateRestaurantInFirebase();
            floatingActionButton.setActivated(true);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.baseline_cancel_black_24).mutate();
            floatingActionButton.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            floatingActionButton.setImageDrawable(drawable);
            this.deleteChoiceRestaurant();
            floatingActionButton.setActivated(false);
        }
    }

    @OnClick(R.id.activity_restaurant_like_button)
    public void OnClickLikeButton() {
        if (floatingActionButton.isActivated() == false) {
            Drawable drawable = getResources().getDrawable(R.drawable.baseline_star_rate_white_24).mutate();
            drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            likeButton.setText("");
            likeButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            this.like = true;
            this.restaurantFiresstore();
            floatingActionButton.setActivated(true);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.baseline_star_rate_white_24).mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            likeButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            likeButton.setText(getString(R.string.like));
            this.like = false;
            this.restaurantFiresstore();
            floatingActionButton.setActivated(false);
        }
    }

    private void configureRestaurant() {

        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User currentUser = documentSnapshot.toObject(User.class);
                restaurantChoice = currentUser.getRestaurantChoiceId();


                if (restautantId.equals(restaurantChoice)) {
                    Drawable drawable = getResources().getDrawable(R.drawable.baseline_done_white_24).mutate();
                    floatingActionButton.getBackground().setColorFilter(getResources().getColor(R.color.quantum_lightgreen),
                            PorterDuff.Mode.SRC_ATOP);
                    floatingActionButton.setImageDrawable(drawable);
                    floatingActionButton.setActivated(true);
                }

            }
        });

    }

    private void configurelike() {
        RestaurantsHelper.getRestaurant(this.getCurrentUser().getUid(), restautantId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Restaurants currentRestaurant = documentSnapshot.toObject(Restaurants.class);

                if (currentRestaurant != null) {

                    Boolean isLike = currentRestaurant.getLike();


                    if (isLike) {
                        Drawable drawable = getResources().getDrawable(R.drawable.baseline_star_rate_white_24).mutate();
                        drawable.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
                        likeButton.setText("");
                        likeButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
                        like = true;
                        floatingActionButton.setActivated(true);
                    }
                }
            }
        });

    }


    private void restaurantFiresstore() {

        RestaurantsHelper.getRestaurant(this.getCurrentUser().getUid(), restautantId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Restaurants currentRestaurant = documentSnapshot.toObject(Restaurants.class);


                if (currentRestaurant == null) {
                    RestaurantsHelper.createRestaurant(getCurrentUser().getUid(), restautantId, nameRestaurant.getText().toString(), like);
                } else {
                    RestaurantsHelper.updateLike(getCurrentUser().getUid(), like, restautantId);
                }
            }

        });

    }


    private void executeHttpRequestWithRetrofit() {
        disposable = PlaceStream.streamDetails(restautantId).subscribeWith(new DisposableObserver<Details>() {
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


    public void update(Details details) {
        this.adress.setText(details.getResult().getFormattedAddress());
        this.nameRestaurant.setText(details.getResult().getName());

        //rating
        Double rating = details.getResult().getRating();
        Integer stars = rating(rating);

        if (stars == 0) {
            stars1.setVisibility(View.INVISIBLE);
            stars2.setVisibility(View.INVISIBLE);
            stars3.setVisibility(View.INVISIBLE);
        }
        if (stars == 1) {
            stars1.setVisibility(View.INVISIBLE);
            stars2.setVisibility(View.INVISIBLE);
        }
        if (stars == 2) {
            stars1.setVisibility(View.INVISIBLE);
        }

        //photo
        if (photo != null) {
            String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo + "&key=" + api_key;
            Glide.with(this).load(photoUrl).into(image);
        } else {
            Glide.with(this).load(R.drawable.restaurant).into(image);
        }


    }

    // 3 - Update User Username
    private void updateRestaurantInFirebase() {


        String restaurant = restautantId;

        if (this.getCurrentUser() != null) {
            if (!restaurant.isEmpty() && !restaurant.equals("no user found")) {
                UserHelper.updateChoiceRestaurant(restaurant, this.getCurrentUser().getUid());
                UserHelper.updateRestaurantPicture(photo, this.getCurrentUser().getUid());
                UserHelper.updateRestaurantName(nameRestaurant.getText().toString(), this.getCurrentUser().getUid());

                Calendar calendar = Calendar.getInstance();
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                UserHelper.updateDate(dayOfYear, this.getCurrentUser().getUid());
            }
        }
    }

    private void deleteChoiceRestaurant() {
        if (this.getCurrentUser() != null) {
            UserHelper.updateChoiceRestaurant(null, this.getCurrentUser().getUid());
            UserHelper.updateRestaurantPicture(null, this.getCurrentUser().getUid());
            UserHelper.updateRestaurantName(null, this.getCurrentUser().getUid());
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

    private void configureRecyclerView ()
    {
        this.adapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getUserByRestaurantId(restautantId)),Glide.with(this),true);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private FirestoreRecyclerOptions<User> generateOptionsForAdapter (Query query)
    {
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();
    }
}
