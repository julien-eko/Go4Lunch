package com.example.go4lunch.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.go4lunch.Notifications.NotificationsAlarmReceiver;
import com.example.go4lunch.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private PendingIntent pendingIntent;
    @BindView(R.id.settings_activity_switch)
    Switch notificationsSwitch;
    @BindView(R.id.home_page_activity_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        this.configureToolBar();
        notificationsSwitch.setOnCheckedChangeListener(this);
        this.configureAlarmManager();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            startAlarm();
        } else {
            stopAlarm();
        }
    }
    //Configuring the AlarmManager and save inforpation in intent for notification message
    private void configureAlarmManager() {
        Intent alarmIntent = new Intent(SettingsActivity.this, NotificationsAlarmReceiver.class);
        //alarmIntent.putExtra("queryShearch", editText.getText().toString());
        //alarmIntent.putExtra("newsDesk", newsDesk());

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent(SettingsActivity.this, HomePageActivity.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    // Start Alarm at 19:00 and repeat all day if actived if one category or more selected
    private void startAlarm() {
        configureAlarmManager();
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //manager.setRepeating(AlarmManager.RTC_WAKEUP, times(12, 0), AlarmManager.INTERVAL_DAY, pendingIntent);

        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,10, pendingIntent);
            Toast.makeText(this, "Alarm set !", Toast.LENGTH_SHORT).show();

    }

    // Stop Alarm
    private void stopAlarm() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled !", Toast.LENGTH_SHORT).show();
    }

    /**
     * use for choose times of notification
     *
     * @param hours  chosse your hours
     * @param minute choose your minutes
     * @return time in millis
     */
    private long times(int hours, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
}
