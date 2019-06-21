package com.example.go4lunch.Models.Firestore;

import androidx.annotation.Nullable;

public class User {

    private String uid;
    private String username;
    @Nullable  private String urlPicture;
    @Nullable  private String restaurantPicture;
    private String restaurantChoiceId;
    private int date;
    private String restaurantName;
    private String restaurantAdress;

    public User() { }

    public User(String uid, String username, String urlPicture,String restaurantChoiceId,int date, String restaurantPicture,String restaurantName,String restaurantAdress) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restaurantChoiceId = restaurantChoiceId;
        this.date = date;
        this.restaurantPicture=restaurantPicture;
        this.restaurantName=restaurantName;
        this.restaurantAdress=restaurantAdress;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getRestaurantChoiceId() { return restaurantChoiceId; }
    public int getDate() { return date; }
    public String getRestaurantPicture() { return restaurantPicture; }
    public String getRestaurantName() { return restaurantName; }
    public String getRestaurantAdress() { return restaurantAdress; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setRestaurantId(String restaurantChoiceId) { this.restaurantChoiceId = restaurantChoiceId; }
    public void setDate(int date) { this.date = date; }
    public void setRestaurantPicture(String restaurantPicture) { this.restaurantPicture = restaurantPicture; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public void setRestaurantAdress(String restaurantAdress) { this.restaurantAdress = restaurantAdress; }
}