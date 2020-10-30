package com.v.flexplayer.playback;

public interface IPlaybackService {

    void play();

    void pause();

    void stop();

    boolean openFile(String path);

    long getDuration();

    long getPosition();

    void seek(long pos);

    boolean isPlaying();

}
