package com.example.go4lunch.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.go4lunch.Models.Search.NearbySearch;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.PlaceService;
import com.example.go4lunch.Utils.PlaceStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class ListViewFragment extends Fragment {

    private Disposable disposable;
    private String test;
    @BindView(R.id.testapi)
    TextView testapi;


    public ListViewFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        this.executeHttpRequestWithRetrofit();


        return view;
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHttpRequestWithRetrofit() {
        this.disposable = PlaceStream.streamNearbySearch("45.515731, -1.126769").subscribeWith(new DisposableObserver<NearbySearch>() {
            @Override
            public void onNext(NearbySearch nearbySearch) {
                test = nearbySearch.getResults().get(1).getName();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG", "Error TopStoriesFragment " + Log.getStackTraceString(e));
                testapi.setText("error");
            }

            @Override
            public void onComplete() {
                testapi.setText(test);
            }
        });
    }
}