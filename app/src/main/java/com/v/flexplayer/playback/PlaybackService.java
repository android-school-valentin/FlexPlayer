package com.v.flexplayer.playback;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.v.flexplayer.MainActivity;
import com.v.flexplayer.R;
import com.v.flexplayer.song.Song;

import java.io.IOException;
import java.util.ArrayList;

import static com.v.flexplayer.App.PLAYBACK_CHANNEL_ID;

public class PlaybackService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, IPlaybackService {

    private MediaPlayer player;
    private ArrayList<Song> songsList;
    private int songPosition;
    private Notification playbackNotification;
    private final PlaybackBinder binder = new PlaybackBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
        initPlayer();
        showNotification();
        startForeground(1, playbackNotification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void initPlayer() {
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    private void showNotification() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, playbackNotification);
    }

    private void initNotification() {

        notificationLayout.setOnClickPendingIntent(R.id.notification_play_button, clickPendingIntent);
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent activityPendingIntent = PendingIntent.getActivity(
                this,
                0,
                activityIntent,
                0);
        playbackNotification = new NotificationCompat.Builder(this, PLAYBACK_CHANNEL_ID)
                .setContentIntent(activityPendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setNotificationSilent()
                .setContentTitle("Song name")
                .setContentText("Artist name")
                .setStyle(new Notification.MediaStyle().setMediaSession(this))
                .build();

    }

    public void setSongsList(ArrayList<Song> songsList) {
        this.songsList = songsList;
    }

    public void setSongPosition(int songPosition) {
        this.songPosition = songPosition;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void play() {
        player.reset();
        Song currentSong = songsList.get(songPosition);
        try {
            player.setDataSource(currentSong.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean openFile(String path) {
        return false;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public long getPosition() {
        return 0;
    }

    @Override
    public void seek(long pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    public class PlaybackBinder extends Binder {
        public PlaybackService getService() {
            return PlaybackService.this;
        }
    }

}
