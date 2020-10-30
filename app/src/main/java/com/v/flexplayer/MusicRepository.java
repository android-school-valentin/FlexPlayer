package com.v.flexplayer;

import android.app.Application;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.v.flexplayer.song.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MusicRepository {
    Application application;

    public MusicRepository(Application application) {
        this.application = application;
    }

    public ArrayList<Song> getAllSongs() {
        try {
            return new GetAllSongsAsyncTask(application).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetAllSongsAsyncTask extends AsyncTask<Void, Void, ArrayList<Song>> {
        Application application;

        private GetAllSongsAsyncTask(Application application) {
            this.application = application;
        }

        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {
            ArrayList<Song> tempList = new ArrayList<>();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Audio.AudioColumns.DATA,
                    MediaStore.Audio.AudioColumns.TITLE,
                    MediaStore.Audio.AudioColumns.ARTIST,
                    MediaStore.Audio.AudioColumns.ALBUM,
                    MediaStore.Audio.AudioColumns.ALBUM_ID,
            };
            Cursor cursor = application.getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(0); //path to a file
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(new File(path).getAbsolutePath());
                    String name = cursor.getString(1); //name of a song
                    String artistName = cursor.getString(2); //name of the artist
                    String albumName = cursor.getString(3); //album name
                    String genre = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE); //genre
                    int dur = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)); //duration
                    long minutes = (dur / 1000)  / 60;
                    int seconds = (dur / 1000) % 60;
                    String duration = String.format(Locale.US, "%02d:%02d", minutes, seconds);
                    String albumId = cursor.getString(4); //album id to find an image
                    Song song = new Song(name, artistName, albumName, genre, path, duration, albumId);
                    tempList.add(song);
                }
                cursor.close();
            }
            return tempList;
        }
    }

}
