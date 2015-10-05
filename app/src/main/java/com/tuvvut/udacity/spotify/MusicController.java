package com.tuvvut.udacity.spotify;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wu on 2015/09/24
 */
public class MusicController implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public final static String PLAY_OR_PAUSE = "MusicController_ACTION_PLAY_OR__PAUSE";
    public final static String STOP = "MusicController_ACTION_STOP";
    public final static String NEXT = "MusicController_ACTION_NEXT";
    public final static String PREVIOUS = "MusicController_ACTION_PREVIOUS";
    public final static String CANCEL = "MusicController_ACTION_CANCEL";

    private static MusicController instance = null;
    private ArrayList<MusicControlListener> listeners = new ArrayList<>();
    private List<Track> songsList;
    private int songIndex = -1;
    private MediaPlayer mediaPlayer;
    private boolean isPreparing = true;
    private boolean isMorePrevious = false, isMoreNext = false;
    private int errorMessageResId;

    private MusicController() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    public void setSongIndex(int songIndex) {
        this.songIndex = songIndex;
    }

    public boolean isMorePrevious() {
        return isMorePrevious;
    }

    public boolean isMoreNext() {
        return isMoreNext;
    }

    public void setSongsList(List<Track> songsList) {
        this.songsList = songsList;
    }

    public Track getTrackForNow() {
        if (songsList != null) {
            return songsList.get(songIndex);
        }
        return null;
    }

    public void register(MusicControlListener listener) {
        listeners.add(listener);
    }

    public void unregister(MusicControlListener listener) {
        listeners.remove(listener);
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        listeners.clear();
        songsList.clear();
        songIndex = -1;
        instance = null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stop();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPreparing = false;
        dispatchAction(ACTION.READY);
        playOrPause();
    }

    public void playOrPause() {
        if (isPreparing) {
            errorMessageResId = R.string.preparing;
            dispatchAction(ACTION.ERROR);
            return;
        }
        if (isPlaying()) {
            mediaPlayer.pause();
            dispatchAction(ACTION.PAUSE);
        } else {
            mediaPlayer.start();
            dispatchAction(ACTION.PLAY);
        }
    }

    public void next() {
        if (isMoreNext) {
            songIndex++;
            reset();
        }
    }

    public void previous() {
        if (isMorePrevious) {
            songIndex--;
            reset();
        }
    }

    public void reset() {
        try {
            isMorePrevious = songIndex > 0;
            isMoreNext = songIndex < songsList.size() - 1;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getTrackForNow().preview_url);
            mediaPlayer.prepareAsync();
            isPreparing = true;
            dispatchAction(ACTION.RESET);
        } catch (Exception e) {
            errorMessageResId = R.string.something_wrong;
            dispatchAction(ACTION.ERROR);
        }
    }

    public void stop() {
        dispatchAction(ACTION.STOP);
    }

    public void cancel(){
        dispatchAction(ACTION.CANCEL);
        mediaPlayer.stop();
    }

    private void dispatchAction(ACTION action) {
        MusicControlListener listener;
        for (int i = 0; i < listeners.size(); i++) {
            listener = listeners.get(i);
            if (listener != null) {
                switch (action) {
                    case RESET:
                        listener.reset();
                        break;
                    case READY:
                        listener.ready();
                        break;
                    case PLAY:
                        listener.play();
                        break;
                    case PAUSE:
                        listener.pause();
                        break;
                    case STOP:
                        listener.stop();
                        break;
                    case CANCEL:
                        listener.cancel();
                        break;
                    case ERROR:
                        listener.error(errorMessageResId);
                        break;
                }
            }
        }
    }

    public boolean isPreparing() {
        return isPreparing;
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getDuration();
    }

    public void seekTo(int sec) {
        mediaPlayer.seekTo(sec);
    }

    public enum ACTION {RESET, READY, PLAY, PAUSE, STOP,CANCEL, ERROR}

    public interface MusicControlListener {
        void reset();

        void ready();

        void play();

        void pause();

        void stop();

        void cancel();

        void error(@StringRes int errorMessageResId);
    }
}
