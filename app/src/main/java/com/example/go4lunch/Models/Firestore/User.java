package com.example.go4lunch.Models.Firestore;

import android.support.annotation.Nullable;

import java.util.Date;

public class User {

    private String uid;
    private String username;
    @Nullable  private String urlPicture;
    @Nullable  private String restaurantPicture;
    private String restaurantChoiceId;
    private int date;
    private String restaurantName;

    public User() { }

    public User(String uid, String username, String urlPicture,String restaurantChoiceId,int date, String restaurantPicture,String restaurantName) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantChoiceId = restaurantChoiceId;
        this.date = date;
        this.restaurantPicture=restaurantPicture;
        this.restaurantName=restaurantName;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getRestaurantChoiceId() { return restaurantChoiceId; }
    public int getDate() { return date; }
    public String getRestaurantPicture() { return restaurantPicture; }
    public String getRestaurantName() { return restaurantName; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantId(String restaurantChoiceId) { this.restaurantChoiceId = restaurantChoiceId; }
    public void setDate(int date) { this.date = date; }
    public void setRestaurantPicture(String restaurantPicture) { this.restaurantPicture = restaurantPicture; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
}