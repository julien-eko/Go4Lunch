package com.example.go4lunch.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;

import com.example.go4lunch.Base.BaseActivity;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends BaseActivity {


    @BindView(R.id.activity_main_google_button)
    Button googleButton;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() { return R.layout.activity_main; }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Handle SignIn Activity response on activity result
            this.handleResponseAfterSignIn(requestCode, resultCode, data);


    }
    // --------------------
    // REST REQUEST
    // --------------------

    // 1 - Http request that create user in firestore
    private void createUserInFirestore() {
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                User currentUser = documentSnapshot.toObject(User.class);

                String urlPicture = (getCurrentUser().getPhotoUrl() != null) ? getCurrentUser().getPhotoUrl().toString() : null;
                String username = getCurrentUser().getDisplayName();
                String uid = getCurrentUser().getUid();

                if (currentUser == null) {
                    UserHelper.createUser(uid, username, urlPicture,null, null,null,0,null);
                }else {
                    Calendar calendar = Calendar.getInstance();
                    int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

                    if (dayOfYear != currentUser.getDate()) {

                        UserHelper.createUser(uid, username, urlPicture, null, null,null,0,null);
                    }
                }
            }

        });

    }
    //-----------------
    //ACTION
    //----------------

    @OnClick(R.id.activity_main_google_button)
    public void onClickGoogleButton(){
        this.startSignInGoogle();
    }

    @OnClick(R.id.activity_main_twitter_button)
    public void onClickTwitterButton(){
        this.startSignInTwitter();
    }
    @OnClick(R.id.activity_main_facebook_button)
    public void onClickFacebookButton(){
        this.startSignInFacebook();
    }


    // --------------------
    // NAVIGATION
    // --------------------

    // 2 - Launch Sign-In Activity
    private void startSignInGoogle(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInFacebook(){
       // this.printHashKey(this);
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInTwitter(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }


    // --------------------
    // UTILS
    // --------------------

    //Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);


        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                //showSnackBar(this.layout, "conection ok");
                this.startActivity();
            } else { // ERRORS
                if (response == null) {

                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {

                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                }
            }
        }
    }


    public void startActivity(){
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }
}
