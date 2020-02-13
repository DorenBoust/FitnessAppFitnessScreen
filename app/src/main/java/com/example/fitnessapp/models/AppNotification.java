package com.example.fitnessapp.models;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class AppNotification extends Application {
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,"קבלת PUSH כשזמן המנוחה נגמר", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("קבלת PUSH כשזמן המנוחה נגמר");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,"קבלת PUSH שהגיע הזמן לאכול", NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription("קבלת PUSH שהגיע הזמן לאכול");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        };
    }
}
