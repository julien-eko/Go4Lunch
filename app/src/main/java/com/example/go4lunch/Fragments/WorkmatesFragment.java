package com.example.go4lunch.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.go4lunch.Activities.RestaurantDetailsActivity;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.example.go4lunch.Utils.ItemClickSupport;
import com.example.go4lunch.Views.WorkmatesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

public class WorkmatesFragment extends Fragment {

    @BindView(R.id.fragment_workamates_view_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_workmates_recycler_view)
    RecyclerView recyclerView;
    private WorkmatesAdapter adapter;

    public WorkmatesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,@NonNull Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();
        this.configureOnClickRecyclerView();
        this.configureSwipeRefreshLayout();
        return view;
    }


    private void configureSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                configureRecyclerView();
            }
        });
    }

    //recyclerView with workmates
    private void configureRecyclerView() {
        //Create adapter passing the list of users
        swipeRefreshLayout.setRefreshing(false);
        this.adapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getAllUsers()), Glide.with(this),false);
        //Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        //Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    //open restaurantDetailsActivity when user click on item
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_workmates_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if(adapter.getItem(position).getRestaurantChoiceId() != null) {

                            Intent restaurantDetails = new Intent(WorkmatesFragment.this.getContext(), RestaurantDetailsActivity.class);
                            restaurantDetails.putExtra("restaurant", adapter.getItem(position).getRestaurantChoiceId());
                            restaurantDetails.putExtra("photo", adapter.getItem(position).getRestaurantPicture());
                            startActivity(restaurantDetails);
                        }
                        }
                });
    }




    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .setLifecycleOwner(this)
                .build();
    }

    //function autocomplete
    public void updateAutocomplete(String restaurantId){
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        this.adapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getUserByRestaurantIdAndDate(restaurantId,dayOfYear)), Glide.with(this),false);
        this.recyclerView.setAdapter(this.adapter);
    }
}