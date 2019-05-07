package com.example.go4lunch.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.Models.Restaurant;
import com.example.go4lunch.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User,WorkmatesViewHolder> {


    private RequestManager glide;
    private Boolean isActivityRestaurant;

    // CONSTRUCTOR
    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options, RequestManager glide,Boolean isActivityRestaurant) {
        super(options);
        this.glide = glide;
        this.isActivityRestaurant=isActivityRestaurant;
    }


    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_workmates_item, parent, false);

        return new WorkmatesViewHolder(context,view,isActivityRestaurant);
    }


    // UPDATE VIEW
    @Override
    protected void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position,@NonNull User model) {
        holder.updateUser(model,this.glide);
    }


}
