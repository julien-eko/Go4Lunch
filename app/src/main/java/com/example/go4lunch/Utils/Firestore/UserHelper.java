package com.example.go4lunch.Utils.Firestore;

import com.example.go4lunch.Models.Firestore.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, String restaurantId, String restaurantPicture, String restaurantName,int date,String restaurantAdress) {

        User userToCreate = new User(uid, username, urlPicture,restaurantId,date,restaurantPicture,restaurantName,restaurantAdress);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }


    public static Query getUserByRestaurantId(String restaurantChoiceId){
        return UserHelper.getUsersCollection().whereEqualTo("restaurantChoiceId",restaurantChoiceId);
    }

    public static Query getUserByRestaurantIdAndDate(String restaurantChoiceId,int date){
        return UserHelper.getUsersCollection().whereEqualTo("restaurantChoiceId",restaurantChoiceId).whereEqualTo("date",date);
    }

    public static Task<QuerySnapshot> getUsersInterestedByRestaurant(String restaurantId)
    {
        return getUserByRestaurantId(restaurantId).get();
    }

    public static Query getAllUsers(){
        return UserHelper.getUsersCollection().orderBy("restaurantChoiceId", Query.Direction.DESCENDING);
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

    public static Task<Void> updateRestaurantName(String restaurantName, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restaurantName", restaurantName);
    }

    public static Task<Void> updateRestaurantAdress(String restaurantAdress, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("restaurantAdress", restaurantAdress);
    }
    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
