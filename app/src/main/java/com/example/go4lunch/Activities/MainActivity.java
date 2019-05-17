package com.example.go4lunch.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.support.annotation.BinderThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    //@BindView(R.id.activity_main_layout)
    //LinearLayout layout;
    @BindView(R.id.activity_main_google_button)
    Button googleButton;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getFragmentLayout() { return R.layout.activity_main; }

/*
    @Override
    protected void onResume() {
        super.onResume();
        // 5 - Update UI when activity is resuming
        this.updateUIWhenResuming();
    }
*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
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
                    UserHelper.createUser(uid, username, urlPicture,null, null,null,0);
                }else {
                    Calendar calendar = Calendar.getInstance();
                    int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

                    if (dayOfYear != currentUser.getDate()) {

                        UserHelper.createUser(uid, username, urlPicture, null, null,null,0);
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
/*
        // 4 - Start appropriate activity
        if (this.isCurrentUserLogged()){
            this.startActivity();
        } else {
            this.startSignInActivity();
        }
*/
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
    // UI
    // --------------------

    // 2 - Show Snack Bar with a message
    private void showSnackBar(LinearLayout Layout, String message){
        //Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
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

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("key", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("key 1", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("key 2", "printHashKey()", e);
        }
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

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.FacebookBuilder().build(),
                                        new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }
    // --------------------
    // UTILS
    // --------------------

    // 3 - Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        //Log.i("co", Integer.toString(requestCode));

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                //showSnackBar(this.layout, "conection ok");
                this.startActivity();
            } else { // ERRORS
                if (response == null) {
                  //  showSnackBar(this.layout, "error");
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    //showSnackBar(this.layout, "no intenet");
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    //showSnackBar(this.layout, "unknow error");
                }
            }
        }
    }

    // 2 - Update UI when activity is resuming
    private void updateUIWhenResuming(){
       // this.googleButton.setText(this.isCurrentUserLogged() ? "Afficher map" : "CONNEXION");
    }

    public void startActivity(){
        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
