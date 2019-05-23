package com.example.go4lunch.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.go4lunch.Base.BaseActivity;
import com.example.go4lunch.Notifications.NotificationsAlarmReceiver;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private PendingIntent pendingIntent;
    @BindView(R.id.settings_activity_switch)
    Switch notificationsSwitch;
    @BindView(R.id.home_page_activity_toolbar)
    Toolbar toolbar;
    private static final int DELETE_USER_TASK = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        this.configureToolBar();

        configureNotificationSwitch();
        this.configureAlarmManager();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_settings;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(getCurrentUser()!=null){
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            preferences.edit().putBoolean(this.getCurrentUser().getUid(), notificationsSwitch.isEnabled()).apply();

        }

    }


    @OnClick(R.id.activity_settings_delete_button)
    public void onCLickDeleteButton() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.choice_delete_account))
                .setPositiveButton(getResources().getString(R.string.popup_message_choice_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteUserFromFirebase();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.popup_message_choice_no), null)
                .show();

    }
    public void configureNotificationSwitch(){
        notificationsSwitch.setOnCheckedChangeListener(this);

        if(getPreferences(MODE_PRIVATE).getBoolean(FirebaseAuth.getInstance().getCurrentUser().getUid(), false)){
            notificationsSwitch.setChecked(true);
        }else{
            notificationsSwitch.setChecked(false);
        }
    }

    //Configuring the AlarmManager and save inforpation in intent for notification message
    private void configureAlarmManager() {
        Intent alarmIntent = new Intent(SettingsActivity.this, NotificationsAlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Log.e("key 1", "alarm manager");
    }

    private void configureToolBar() {
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_white_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");
    }

    //configure button return of toolbar
    //bug when use logout after

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            startAlarm();
        } else {
            stopAlarm();
        }
    }



    // Start Alarm at 19:00 and repeat all day if actived if one category or more selected
    private void startAlarm() {
        configureAlarmManager();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, times(), AlarmManager.INTERVAL_DAY, pendingIntent);

        //manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, 10, pendingIntent);
        Toast.makeText(this, getResources().getString(R.string.notification_enable), Toast.LENGTH_SHORT).show();

    }

    // Stop Alarm
    private void stopAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, getResources().getString(R.string.notification_desable), Toast.LENGTH_SHORT).show();
    }

    /**
     * use for choose times of notification
     *
     * @return time in millis
     */
    private long times() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    private void deleteUserFromFirebase(){
        if (getCurrentUser() != null) {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            preferences.edit().putBoolean(this.getCurrentUser().getUid(),false);
            UserHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());

            AuthUI.getInstance()
                    .delete(this)
                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
        }
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){

                    case DELETE_USER_TASK:
                        Intent mainActivity = new Intent(SettingsActivity.this,MainActivity.class);
                        startActivity(mainActivity);
                        break;
                    default:
                        break;
                }
            }
        };
    }
}

