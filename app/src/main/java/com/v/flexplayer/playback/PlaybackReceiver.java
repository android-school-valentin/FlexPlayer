package com.v.flexplayer.playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PlaybackReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "YOU CLICKED BUTTON", Toast.LENGTH_SHORT).show();
    }
}
