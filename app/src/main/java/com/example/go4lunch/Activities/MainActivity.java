package com.example.go4lunch.Activities;

import android.content.Intent;
import android.support.annotation.BinderThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.go4lunch.Base.BaseActivity;
import com.example.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.activity_main_layout)
    LinearLayout layout;
    @BindView(R.id.activity_main_google_button)
    Button googleButton;
    private static final int RC_SIGN_IN = 123;

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
        this.startSignInActivity();
    }


    // --------------------
    // UI
    // --------------------

    // 2 - Show Snack Bar with a message
    private void showSnackBar(LinearLayout Layout, String message){
        Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
    }

// --------------------
    // NAVIGATION
    // --------------------

    // 2 - Launch Sign-In Activity
    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))
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

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.layout, "conection ok");
                this.startActivity();
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.layout, "error");
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.layout, "no intenet");
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.layout, "unknow error");
                }
            }
        }
    }

    // 2 - Update UI when activity is resuming
    private void updateUIWhenResuming(){
        this.googleButton.setText(this.isCurrentUserLogged() ? "Afficher map" : "CONNEXION");
    }

    public void startActivity(){
        Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
