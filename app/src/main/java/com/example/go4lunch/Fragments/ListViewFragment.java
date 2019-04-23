package com.example.go4lunch.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.go4lunch.Models.ListRestaurant;
import com.example.go4lunch.Models.Restaurant;
import com.example.go4lunch.Models.Search.NearbySearch;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.PlaceStream;
import com.example.go4lunch.Views.PlaceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListViewFragment extends Fragment {

    private Disposable disposable;
    private List<Restaurant> list;
    private PlaceAdapter adapter;
    @BindView(R.id.fragment_list_view_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_list_view_recycler_view)
    RecyclerView recyclerView;


    public ListViewFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerView();
        //Execute stream after UI creation
        this.executeHttpRequestWithRetrofit();
        //Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    private void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
    }

    //Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView() {
        //Reset list
        this.list = new ArrayList<>();
        //Create adapter passing the list of users
        this.adapter = new PlaceAdapter(this.list, Glide.with(this));
        //Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        //Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHttpRequestWithRetrofit() {
        this.disposable = PlaceStream.streamNearbySearch("45.515731, -1.126769").subscribeWith(new DisposableObserver<NearbySearch>() {
            @Override
            public void onNext(NearbySearch nearbySearch) {
                updateUI(nearbySearch);
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

    //update with list of aticles
    private void updateUI(NearbySearch nearbySearch) {
        swipeRefreshLayout.setRefreshing(false);
        this.list.clear();
        ListRestaurant.ListNearbySearch(this.list,nearbySearch);
        adapter.notifyDataSetChanged();
    }
}