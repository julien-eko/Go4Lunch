package com.example.go4lunch.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.go4lunch.Activities.HomePageActivity;
import com.example.go4lunch.Activities.RestaurantDetailsActivity;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.example.go4lunch.Utils.ItemClickSupport;
import com.example.go4lunch.Views.PlaceAdapter;
import com.example.go4lunch.Views.WorkmatesAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

public class WorkmatesFragment extends Fragment {

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
        return view;
    }


    private void configureRecyclerView() {
        //Reset list
        //this.list = new ArrayList<>();
        //Create adapter passing the list of users
        this.adapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getAllUsers()), Glide.with(this),false);
        //Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        //Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

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

}