package com.example.cocktailrecipebook;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // show toast
//        Toast.makeText(context, "Alarm running", Toast.LENGTH_LONG).show();
        Log.d("GARY", "Received");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NotificationHelper.CHANNEL_NAME);
        int id = intent.getIntExtra(NotificationHelper.ID, 0);
        notificationManager.notify(id, notification);
    }
}