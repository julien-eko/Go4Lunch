package com.example.go4lunch.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.Models.Restaurant;
import com.example.go4lunch.R;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private List<Restaurant> list;
    private RequestManager glide;

    // CONSTRUCTOR
    public PlaceAdapter(List<Restaurant> list, RequestManager glide) {
        this.list = list;
        this.glide = glide;
    }


    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_restaurant_item, parent, false);

        return new PlaceViewHolder(view);
    }

    // UPDATE VIEW
    @Override
    public void onBindViewHolder(PlaceViewHolder viewHolder, int position) {
        viewHolder.updateWithTimesUser(this.list.get(position), this.glide);
    }



    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
