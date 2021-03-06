package com.example.go4lunch.Activities;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.Base.BaseActivity;
import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Fragments.ListViewFragment;
import com.example.go4lunch.Fragments.MapViewFragment;
import com.example.go4lunch.Fragments.WorkmatesFragment;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePageActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.home_page_activity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_page_activity_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.home_page_activity_nav_view)
    NavigationView navigationView;
    @BindView(R.id.home_page_activity_frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.home_page_activity_list_view_button)
    Button listViewButton;
    @BindView(R.id.home_page_activity_map_button)
    Button mapViewButton;
    @BindView(R.id.home_page_activity_workmates_button)
    Button workmatesButton;
    private TextView email;
    private TextView name;
    private ImageView image;
    private static final int SIGN_OUT_TASK = 10;
    private int AUTOCOMPLETE_REQUEST_CODE = 1;
    private List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
    private MapViewFragment mapViewFragment;
    private ListViewFragment listViewFragment;
    private WorkmatesFragment workmatesFragment;



    @Override
    public int getFragmentLayout() {
        return R.layout.activity_home_page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ButterKnife.bind(this);

        View hView = navigationView.inflateHeaderView(R.layout.home_page_nav_header);

        Places.initialize(getApplicationContext(), BuildConfig.ApiKey);

        email = (TextView) hView.findViewById(R.id.home_page_activity_email);
        name = (TextView) hView.findViewById(R.id.home_page_activity_name);
        image = (ImageView) hView.findViewById(R.id.home_page_activity_photo);

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.configureFragment();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_toolbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                this.launchAutocompleteActivity();
                break;

        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.home_page_menu_your_lunch:
                this.yourLunch();
                break;
            case R.id.home_page_menu_settings:
                Intent settingsActivity = new Intent(HomePageActivity.this, SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.home_page_menu_logout:
                this.signOutUserFromFirebase();
                break;


        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //open restaurantdetailsActivity if user choose restaurant else show toast message
    private void yourLunch() {
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User currentUser = documentSnapshot.toObject(User.class);
                String restaurantChoiceId = currentUser.getRestaurantChoiceId();
                String restaurantPicture = currentUser.getRestaurantPicture();

                if(restaurantChoiceId != null) {

                    Intent restaurantDetails = new Intent(HomePageActivity.this, RestaurantDetailsActivity.class);
                    restaurantDetails.putExtra("restaurant", restaurantChoiceId);
                    restaurantDetails.putExtra("photo", restaurantPicture);
                    startActivity(restaurantDetails);
                }else{
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.No_restaurant),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //SignOut
    private void signOutUserFromFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }


    //  Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    // --------------
    // Configure
    // --------------
    private void configureToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.map_list_title));
    }

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Configure NavigationView
    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);

        if (this.getCurrentUser() != null) {

            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(image);
            }

            //Get email & username from Firebase
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? "" : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getResources().getString(R.string.no_name_found) : this.getCurrentUser().getDisplayName();

            this.name.setText(username);

            this.email.setText(email);


        }
    }

    // --------------
    // Action
    // --------------
    @OnClick(R.id.home_page_activity_map_button)
    public void onClickMapView() {
        toolbar.setTitle(getResources().getString(R.string.map_list_title));
        this.configureAndShowMapViewFragment();

        mapViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        listViewButton.setTextColor(getResources().getColor(R.color.black));
        workmatesButton.setTextColor(getResources().getColor(R.color.black));

        Drawable drawable = getResources().getDrawable(R.drawable.baseline_map_black_24).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        listViewButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_view_list_black_24), null, null);
        workmatesButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_people_black_24), null, null);

    }

    @OnClick(R.id.home_page_activity_list_view_button)
    public void onClickListView() {
            toolbar.setTitle(getResources().getString(R.string.map_list_title));
            this.configureAndShowListViewFragment();
            mapViewButton.setTextColor(getResources().getColor(R.color.black));
            listViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            workmatesButton.setTextColor(getResources().getColor(R.color.black));

            Drawable drawable = getResources().getDrawable(R.drawable.baseline_view_list_black_24).mutate();
            drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
            mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_map_black_24), null, null);
            listViewButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            workmatesButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_people_black_24), null, null);

    }

    @OnClick(R.id.home_page_activity_workmates_button)
    public void onClickWokmates() {
        toolbar.setTitle(getResources().getString(R.string.workmates_title));
        this.configureAndShowWorkmatesFragment();
        mapViewButton.setTextColor(getResources().getColor(R.color.black));
        listViewButton.setTextColor(getResources().getColor(R.color.black));
        workmatesButton.setTextColor(getResources().getColor(R.color.colorPrimary));

        Drawable drawable = getResources().getDrawable(R.drawable.baseline_people_black_24).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_map_black_24), null, null);
        listViewButton.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_view_list_black_24), null, null);
        workmatesButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

    }

    // --------------
    // FRAGMENTS
    // --------------
    private void configureFragment() {
        sleepbutton(listViewButton,workmatesButton);
        mapViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        Drawable drawable = getResources().getDrawable(R.drawable.baseline_map_black_24).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        mapViewFragment = new MapViewFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.home_page_activity_frame_layout, mapViewFragment)
                .commit();
    }


    private void configureAndShowMapViewFragment() {
        sleepbutton(listViewButton,workmatesButton);
        mapViewFragment = new MapViewFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_page_activity_frame_layout, mapViewFragment,"mapViewFragment")
                .commit();

    }

    private void configureAndShowListViewFragment() {
         listViewFragment = new ListViewFragment();
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_page_activity_frame_layout, listViewFragment,"listViewFragment")
                .commit();

    }

    private void configureAndShowWorkmatesFragment() {
        workmatesFragment = new WorkmatesFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_page_activity_frame_layout, workmatesFragment,"WorkmatesFragment")
                .commit();

    }



//AutoComplete
    private void launchAutocompleteActivity() {

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,fields)
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                if(mapViewFragment != null && mapViewFragment.isVisible()){
                    mapViewFragment.updateAutocomplete(place.getLatLng().latitude,place.getLatLng().longitude);

                }
                else if(listViewFragment != null && listViewFragment.isVisible() ){
                    listViewFragment.updateAutocomplete(place.getLatLng().latitude,place.getLatLng().longitude);

                }else{
                    workmatesFragment.updateAutocomplete(place.getId());

                }

                Log.i("place", "Place: " + place.getLatLng().longitude + ", " + place.getLatLng().latitude);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("error autocomplete", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void sleepbutton(Button button1,Button button2){
        button1.setEnabled(false);
        button1.setText(getResources().getString(R.string.loading));
        button2.setEnabled(false);
        button2.setText(getResources().getString(R.string.loading));

        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        button1.setEnabled(true);
                        button1.setText(getResources().getString(R.string.list_view));
                        button2.setEnabled(true);
                        button2.setText(getResources().getString(R.string.workmate));
                    }
                });
            }
        }, 3000);
    }
}
