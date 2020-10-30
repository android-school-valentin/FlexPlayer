package com.v.flexplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;
import com.v.flexplayer.playback.PlaybackService;
import com.v.flexplayer.song.Song;
import com.v.flexplayer.song.SongsFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SongsFragment.OnSongSelectedListener {

    //service require
    private ServiceConnection playbackConnection;
    private PlaybackService playbackService;
    private Intent playIntent;
    private boolean playbackBound = false;

    private LiveData<ArrayList<Song>> songListData;


    //Bottom sheet peek
    private ProgressBar peekProgressBar;
    private ImageView peekAlbumImage;
    private ImageButton peekPlayControlButton;
    private TextView peekSongName;
    private TextView peekArtistName;

    //Bottom sheet content
    private TextView contentSongName;
    private TextView contentArtistName;
    private Slider contentSongLengthSlider;
    private ImageButton contentPlayPreviousButton;
    private ImageButton contentPlayControlButton;
    private ImageButton contentPlayNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabs();
        initBottomSheet();
        SharedMusicViewModel model = ViewModelProviders.of(this).get(SharedMusicViewModel.class);
        songListData = model.getSongList();
        initServiceConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playIntent = new Intent(this, PlaybackService.class);
        bindService(playIntent, playbackConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);
    }

    public void initBottomSheet() {
        //boilerplate initialize bottom sheet
        peekProgressBar = findViewById(R.id.peek_progress_bar);
        peekAlbumImage = findViewById(R.id.peek_album_image);
        peekPlayControlButton = findViewById(R.id.peek_play_control_button);
        peekSongName = findViewById(R.id.peek_song_name);
        peekArtistName = findViewById(R.id.peek_artist_name);

        contentSongName = findViewById(R.id.content_song_name);
        contentArtistName = findViewById(R.id.content_artist_name);
        contentSongLengthSlider = findViewById(R.id.content_song_length_slider);
        contentPlayPreviousButton = findViewById(R.id.content_play_previous_button);
        contentPlayControlButton = findViewById(R.id.content_play_control_button);
        contentPlayNextButton = findViewById(R.id.content_play_next_button);

        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        final RelativeLayout bottomSheetPeek = findViewById(R.id.bottom_sheet_peek);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        bottomSheetPeek.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        bottomSheetPeek.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void initTabs() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), 0);
        tabAdapter.addFragment(new SongsFragment(), "Songs");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void initServiceConnection() {
        playbackConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlaybackService.PlaybackBinder binder = (PlaybackService.PlaybackBinder) iBinder;
                playbackService = binder.getService();
                playbackService.setSongsList(songListData.getValue());
                playbackBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                playbackBound = false;
            }
        };
    }

    //callback from SongsFragment tells us about chosen song
    @Override
    public void onSongSelected(int position) {
        Toast.makeText(this, songListData.getValue().get(position).getPath(), Toast.LENGTH_SHORT)
                .show();
        playbackService.setSongPosition(position);
        playbackService.play();
    }
}