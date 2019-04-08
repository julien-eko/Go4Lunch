package com.example.go4lunch.Activities;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.go4lunch.Fragments.ListViewFragment;
import com.example.go4lunch.Fragments.MapViewFragment;
import com.example.go4lunch.Fragments.WorkmatesFragment;
import com.example.go4lunch.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ButterKnife.bind(this);

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

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void configureToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Go4Lunch");
    }

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Configure NavigationView
    private void configureNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }
    // --------------
    // Action
    // --------------
    @OnClick(R.id.home_page_activity_map_button)
    public void onClickMapView(){
        this.configureAndShowMapViewFragment();



        mapViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        listViewButton.setTextColor(getResources().getColor(R.color.black));
        workmatesButton.setTextColor(getResources().getColor(R.color.black));

        Drawable drawable = getResources().getDrawable(R.drawable.baseline_map_black_24).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
        listViewButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.baseline_view_list_black_24),null,null);
        workmatesButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.baseline_people_black_24),null,null);

    }

    @OnClick(R.id.home_page_activity_list_view_button)
    public void onClickListView(){
        this.configureAndShowListViewFragment();
        mapViewButton.setTextColor(getResources().getColor(R.color.black));
        listViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        workmatesButton.setTextColor(getResources().getColor(R.color.black));

        Drawable drawable = getResources().getDrawable(R.drawable.baseline_view_list_black_24).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.baseline_map_black_24),null,null);
        listViewButton.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
        workmatesButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.baseline_people_black_24),null,null);
    }

    @OnClick(R.id.home_page_activity_workmates_button)
    public void onClickWokmates(){
        this.configureAndShowWorkmatesFragment();
        mapViewButton.setTextColor(getResources().getColor(R.color.black));
        listViewButton.setTextColor(getResources().getColor(R.color.black));
        workmatesButton.setTextColor(getResources().getColor(R.color.colorPrimary));

        Drawable drawable = getResources().getDrawable(R.drawable.baseline_people_black_24).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.baseline_map_black_24),null,null);
        listViewButton.setCompoundDrawablesWithIntrinsicBounds(null,getResources().getDrawable(R.drawable.baseline_view_list_black_24),null,null);
        workmatesButton.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);

    }

    // --------------
    // FRAGMENTS
    // --------------
    private void configureFragment(){
        mapViewButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        Drawable drawable = getResources().getDrawable(R.drawable.baseline_map_black_24).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        mapViewButton.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);

        MapViewFragment mapViewFragment = new MapViewFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.home_page_activity_frame_layout, mapViewFragment)
                .commit();
    }


    private void configureAndShowMapViewFragment(){
        MapViewFragment mapViewFragment = new MapViewFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_page_activity_frame_layout, mapViewFragment)
                .commit();

    }

    private void configureAndShowListViewFragment(){
        ListViewFragment listViewFragment=new ListViewFragment();
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.home_page_activity_frame_layout, listViewFragment)
                    .commit();

    }

    private void configureAndShowWorkmatesFragment(){
        WorkmatesFragment workmatesFragment =new WorkmatesFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_page_activity_frame_layout,workmatesFragment)
                .commit();

    }
}
