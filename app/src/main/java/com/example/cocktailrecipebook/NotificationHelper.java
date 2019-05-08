package com.example.cocktailrecipebook;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.util.Log;

public class NotificationHelper extends ContextWrapper {
    private static final String ID = "come.example.cocktailrecipebook.NotificationHelper";
    private static final String CHANNEL_NAME = "COCKTAIL_CHANNEL";
    private NotificationManager manager;
    public NotificationHelper(Context base){
        super(base);
        Log.d("Alarmy","entered notifhelper");
        createChannels();
    }



    private void createChannels(){
        NotificationChannel cocktail_channel = new NotificationChannel(ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        cocktail_channel.enableLights(true);
        cocktail_channel.enableVibration(true);
        cocktail_channel.setLightColor(Color.GREEN);
        cocktail_channel.setLockscreenVisibility(NotificationHelper.RECEIVER_VISIBLE_TO_INSTANT_APPS);

        getManager().createNotificationChannel(cocktail_channel);
    }
    public NotificationManager getManager() {
        if(manager==null){
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public Notification.Builder getCocktail_channel_notification(String title, String body){
        return new Notification.Builder(getApplicationContext(),ID)
                .setContentText(body)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true);
    }
}
