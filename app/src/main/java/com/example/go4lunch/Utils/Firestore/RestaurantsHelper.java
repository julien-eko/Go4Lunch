package com.example.go4lunch.Utils.Firestore;

import com.example.go4lunch.Models.Firestore.Restaurants;
import com.example.go4lunch.Models.Firestore.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantsHelper {

    private static final String COLLECTION_NAME = "restaurants";



    // --- CREATE ---

    public static Task<Void> createRestaurant(String uid, String rId, String restaurantName, Boolean like ) {
        Restaurants restaurantToCreate = new Restaurants(rId, restaurantName, like);
        return UserHelper.getUsersCollection().document(uid).collection(COLLECTION_NAME).document(rId).set(restaurantToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getRestaurant(String uid, String rId){
        return UserHelper.getUsersCollection().document(uid).collection(COLLECTION_NAME).document(rId).get();
    }


    // --- UPDATE ---


    public static Task<Void> updateLike(String uid, Boolean like, String rId) {
        return  UserHelper.getUsersCollection().document(uid).collection(COLLECTION_NAME).document(rId).update("like", like);
    }



    // --- DELETE ---

    public static Task<Void> deleteRestaurant(String uid, String rId) {
        return UserHelper.getUsersCollection().document(uid).collection(COLLECTION_NAME).document(rId).delete();
    }
}
