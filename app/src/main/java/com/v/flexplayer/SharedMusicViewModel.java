package com.v.flexplayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.v.flexplayer.song.Song;

import java.util.ArrayList;

public class SharedMusicViewModel extends AndroidViewModel {

    private MusicRepository repository;

    private MutableLiveData<ArrayList<Song>> songList;

    public SharedMusicViewModel(@NonNull Application application) {
        super(application);
        repository = new MusicRepository(application);
    }

    public LiveData<ArrayList<Song>> getSongList() {
        if (songList == null) {
            songList = new MutableLiveData<>();
            loadData();
        }
        return songList;
    }

    private void loadData() {
        songList.postValue(repository.getAllSongs());
    }

}
