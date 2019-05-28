package com.example.go4lunch.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.example.go4lunch.Activities.RestaurantDetailsActivity;
import com.example.go4lunch.Models.Search.NearbySearch;
import com.example.go4lunch.Models.Search.Result;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.example.go4lunch.Utils.PlaceStream;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.android.volley.VolleyLog.TAG;


public class MapViewFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {


    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;




    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private static Location mLastKnownLocation;

    private GoogleMap mMap;
    private ImageButton positionButton;

    private Disposable disposable;

    private List<Result> listRestaurant = new ArrayList<>();
    private HashMap<String, Result> markerMap = new HashMap<>();
    private Marker marker;


    public static MapViewFragment newInstance() {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_map_view, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getContext());

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext());

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        positionButton = (ImageButton) view.findViewById(R.id.position_button);
        positionButton.setOnClickListener(this);


        return view;
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    //center map on my position
    @Override
    public void onClick(View v) {
        // Get the current location of the device and set the position of the map.
        mMap.clear();
        getDeviceLocation();

    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            executeHttpRequestWithRetrofit(Double.toString(mLastKnownLocation.getLatitude()) + "," + Double.toString(mLastKnownLocation.getLongitude()));


                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    public void executeHttpRequestWithRetrofit(String location) {

        this.disposable = PlaceStream.streamNearbySearch(location).subscribeWith(new DisposableObserver<NearbySearch>() {
            @Override
            public void onNext(NearbySearch nearbySearch) {
                addMarker(nearbySearch.getResults());


            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    //add a marker to each restaurant on the map
    private void addMarker(List<Result> results) {

        this.listRestaurant.addAll(results);
        mMap.setOnMarkerClickListener(this);
        if (listRestaurant.size() != 0 || listRestaurant != null) {
            for (int i = 0; i < listRestaurant.size(); i++) {
                if (listRestaurant.get(i) != null) {

                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(listRestaurant.get(i).getGeometry().getLocation().getLat(),
                                    listRestaurant.get(i).getGeometry().getLocation().getLng()))
                            .title(listRestaurant.get(i).getName())
                            .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.restaurant_marker_orange)));

                    this.changeMarker(listRestaurant.get(i).getPlaceId(), marker);
                    // Store in HashMap for Marker id for clickHandler

                    this.markerMap.put(marker.getId(), listRestaurant.get(i));
                }
            }

        } else {
            Log.d(TAG, "addMarkerOnMap is empty :" + listRestaurant.size());
        }
    }


    //if a workmate has already planned to go to lunch at a restaurant changes the color of the marker
    private void changeMarker(String restaurantId, final Marker marker) {

        UserHelper.getUsersInterestedByRestaurant(restaurantId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                Calendar calendar = Calendar.getInstance();
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                int j = 0;
                for (int i = 0; i < querySnapshot.size(); i++) {

                    if (querySnapshot.getDocuments().get(i).get("date").toString().equals(Integer.toString(dayOfYear))) {
                        if (!querySnapshot.getDocuments().get(i).get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            j = j + 1;
                        }

                    }
                }

                if (j > 0) {
                    marker.setIcon(bitmapDescriptorFromVector(getActivity(), R.drawable.restaurant_marker_green));
                }


            }
        });
    }

    //when user click on marker open restaurantDetailActivity
    @Override
    public boolean onMarkerClick(final Marker marker) {


        Result result = this.markerMap.get(marker.getId());
        String photo;
        if (result.getPhotos() != null) {
            if (result.getPhotos().get(0).getPhotoReference() != null) {
                photo = result.getPhotos().get(0).getPhotoReference();
            } else {
                photo = null;
            }
        } else {
            photo = null;
        }

        //Toast.makeText(getActivity(),photo, Toast.LENGTH_LONG).show();
        Intent restaurantDetails = new Intent(MapViewFragment.this.getActivity(), RestaurantDetailsActivity.class);
        restaurantDetails.putExtra("restaurant", result.getPlaceId());
        restaurantDetails.putExtra("photo", photo);
        startActivity(restaurantDetails);

        return true;
    }

    //change icon marker
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //function autocomplete
    public void updateAutocomplete(Double latitude, Double longitude) {
        mMap.clear();
        markerMap.clear();
        this.listRestaurant.clear();

        mLastKnownLocation.setLatitude(latitude);
        mLastKnownLocation.setLongitude(longitude);

        this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), DEFAULT_ZOOM));
        executeHttpRequestWithRetrofit(Double.toString(latitude) + "," + Double.toString(longitude));



    }

    public static Location getmLastKnownLocation() {
        return mLastKnownLocation;
    }
}