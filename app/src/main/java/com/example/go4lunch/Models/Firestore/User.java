package com.example.go4lunch.Models.Firestore;

import android.support.annotation.Nullable;

import java.util.Date;

public class User {

    private String uid;
    private String username;
    @Nullable  private String urlPicture;
    private String restaurantChoiceId;
    private Date date;

    public User() { }

    public User(String uid, String username, String urlPicture,String restaurantChoiceId,Date date) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantChoiceId = restaurantChoiceId;
        this.date = date;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getRestaurantChoiceId() { return restaurantChoiceId; }
    public Date getDate() { return date; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantId(String restaurantChoiceId) { this.restaurantChoiceId = restaurantChoiceId; }
    public void setDate(Date date) { this.date = date; }
}