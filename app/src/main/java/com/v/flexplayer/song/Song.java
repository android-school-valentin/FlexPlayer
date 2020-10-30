package com.v.flexplayer.song;

import android.content.ContentUris;
import android.net.Uri;

public class Song {
    private String name;
    private String artistName;
    private String albumName;
    private String genre;
    private String path;
    private String duration;
    private String albumId;

    public Song(String name, String artistName, String albumName, String genre, String path, String duration, String albumId) {
        this.name = name;
        this.artistName = artistName;
        this.albumName = albumName;
        this.genre = genre;
        this.path = path;
        this.duration = duration;
        this.albumId = albumId;
    }

    public Uri getAlbumImageUri() {
        Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(artworkUri, Long.parseLong(albumId));
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getGenre() {
        return genre;
    }

    public String getPath() {
        return path;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbumId() {
        return albumId;
    }
}
