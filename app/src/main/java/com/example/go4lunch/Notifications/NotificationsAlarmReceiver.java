package com.example.go4lunch.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.go4lunch.Activities.MainActivity;
import com.example.go4lunch.Models.Firestore.User;
import com.example.go4lunch.R;
import com.example.go4lunch.Utils.Firestore.UserHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;


public class NotificationsAlarmReceiver extends BroadcastReceiver {

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "FIREBASEOC";
    private String restaurantName;
    private String restaurantAdress;
    private String listWorkmates;
    private Boolean isLunch;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        restaurantInfo();
        //Log.e("key 1", "notification");
       // sendVisualNotification("test","bonjour");
    }

    // ---

    private void restaurantInfo(){
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Calendar calendar = Calendar.getInstance();
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                User currentUser = documentSnapshot.toObject(User.class);
                 restaurantName = currentUser.getRestaurantName();
                 restaurantAdress = currentUser.getRestaurantAdress();
                 if(currentUser.getDate() == dayOfYear && restaurantName != null){
                     isLunch=true;
                 }else{
                     isLunch=false;
                 }
                 workamateList(currentUser.getRestaurantChoiceId());



            }
        });

    }

    private void workamateList(String restaurantId){
        UserHelper.getUsersInterestedByRestaurant(restaurantId).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                Calendar calendar = Calendar.getInstance();
                int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
                for (int i = 0; i < querySnapshot.size(); i++) {

                    if (querySnapshot.getDocuments().get(i).get("date").toString().equals(Integer.toString(dayOfYear))) {
                        if (!querySnapshot.getDocuments().get(i).get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                           if(listWorkmates == null){
                               listWorkmates = "" + querySnapshot.getDocuments().get(i).get("username");
                           }else{
                               listWorkmates =listWorkmates + " "+ querySnapshot.getDocuments().get(i).get("username");
                           }

                    }

                    }
                }
                sendNotification();




            }
        });
    }

    private void sendNotification(){
        if(isLunch){
            String title = "Your lunch: " +restaurantName;
            String message = "with workmate :" + listWorkmates +". Adress: " +restaurantAdress;
            sendVisualNotification(title,message);
        }
    }
    private void sendVisualNotification(String title,String messageBody) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this.context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        inboxStyle.addLine(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = "notification_channel_id";

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this.context, channelId)
                        .setSmallIcon(R.drawable.logo_restaurant)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
