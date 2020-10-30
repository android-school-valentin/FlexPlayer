package com.v.flexplayer.song;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.v.flexplayer.MusicRepository;
import com.v.flexplayer.R;
import com.v.flexplayer.SharedMusicViewModel;

import java.util.ArrayList;


public class SongsFragment extends Fragment implements SongsAdapter.OnItemClickListener {

    private SharedMusicViewModel model;
    private MusicRepository repository;
    private LiveData<ArrayList<Song>> songListData;
    private OnSongSelectedListener onSongSelectedListener;

    public SongsFragment() {
        // Required empty public constructor
    }

    public static SongsFragment newInstance() {
        return new SongsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new MusicRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_songs, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.song_recycler_view);
        model = ViewModelProviders.of(getActivity()).get(SharedMusicViewModel.class);
        onSongSelectedListener = (OnSongSelectedListener) getActivity();
        songListData = model.getSongList();
        SongsAdapter adapter = new SongsAdapter(songListData.getValue());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void OnItemClick(int position) {
        String name = songListData.getValue().get(position).getName();
        Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
        onSongSelectedListener.onSongSelected(position);
    }

    public interface OnSongSelectedListener {
        void onSongSelected(int position);
    }

}