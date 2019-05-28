package com.example.go4lunch.Models.Firestore;



public class Restaurants {

    private String rId;
    private String restaurantName;
    private Boolean like;


    public Restaurants() { }

    public Restaurants(String rId, String restaurantName, Boolean like) {
        this.rId = rId;
        this.restaurantName = restaurantName;
        this.like = like;
    }

    // --- GETTERS ---
    public String getRId() { return rId; }
    public String getRestaurantName() { return restaurantName; }
    public Boolean getLike() { return like; }


    // --- SETTERS ---
    public void setRId(String rId) { this.rId = rId; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public void setLike(Boolean like) { this.like = like; }

}
