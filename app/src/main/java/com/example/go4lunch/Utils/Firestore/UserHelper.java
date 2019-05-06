package com.example.go4lunch.Utils.Firestore;

import com.example.go4lunch.Models.Firestore.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, String restaurantId, int date, String restaurantPicture) {
        User userToCreate = new User(uid, username, urlPicture,restaurantId,date,restaurantPicture);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<DocumentSnapshot> getChoiceRestaurant(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateChoiceRestaurant(String restaurantChoiceId, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restaurantChoiceId", restaurantChoiceId);
    }

    public static Task<Void> updateDate(int date, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("date", date);
    }

    public static Task<Void> updateRestaurantPicture(String restaurantPicture, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restaurantPicture", restaurantPicture);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
