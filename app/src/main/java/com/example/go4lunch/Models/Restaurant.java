package com.example.go4lunch.Models;

public class Restaurant {

    private String nameRestaurant;
    private String  distance;
    private String image;
    private String type;
    private String address;
    private int worksmates;
    private String schedule;
    private Integer stars;

    public Restaurant() {

    }


    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String  distance) {
        this.distance = distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getWorksmates() {
        return worksmates;
    }

    public void setWorksmates(int worksmates) {
        this.worksmates = worksmates;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }


}
