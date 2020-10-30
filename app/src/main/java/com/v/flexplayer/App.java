package com.v.flexplayer;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String PLAYBACK_CHANNEL_ID = "playbackServiceChannel";
    public static final String PLAYBACK_CHANNEL_NAME = "Playback channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel playbackServiceChannel = new NotificationChannel(
                    PLAYBACK_CHANNEL_ID,
                    PLAYBACK_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(playbackServiceChannel);
        }

    }

}
