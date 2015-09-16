package com.tuvvut.udacity.spotify.presenter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.Toast;

import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.fragment.StreamerFragment;

import java.util.List;
import java.util.Locale;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wu on 2015/06/14
 */
public class StreamerPresenter extends Presenter implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private StreamerFragment fragment;
    private List<Track> tracks;
    private int index;
    private boolean isPreparing = true;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable update = new Runnable() {
        @Override
        public void run() {
            int seconds = Math.round((float) (mediaPlayer.getCurrentPosition()) / 1000);
            fragment.setSeekBarProgress(seconds);
            fragment.setStartTime(secondsToDuration(seconds));
            if (mediaPlayer.isPlaying()) {
                handler.postDelayed(update, 200);
            }
        }
    };

    public StreamerPresenter(StreamerFragment f, List<Track> tracks, int index) {
        super(f);
        fragment = f;
        this.tracks = tracks;
        this.index = index;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private Track getTrackForNow() {
        return tracks.get(index);
    }

    public void previous() {
        if (index > 0) {
            index--;
            showTrackInfo();
        } else {
            showToast(R.string.error_first_song, Toast.LENGTH_SHORT);
        }
    }

    public void play() {
        if (isPreparing) {
            showToast(R.string.preparing, Toast.LENGTH_SHORT);
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
            handler.postDelayed(update, 200);
        }
        fragment.setPlayButtonImage(mediaPlayer.isPlaying());
    }

    public void next() {
        if (index < tracks.size() - 1) {
            index++;
            showTrackInfo();
        } else {
            showToast(R.string.error_last_song, Toast.LENGTH_SHORT);
        }
    }

    public void showTrackInfo() {
        Track track = getTrackForNow();
        fragment.setTrackInfo(track);
        fragment.setPreviousButton(index != 0);
        fragment.setNextButton(index != tracks.size() - 1);
        try {
            isPreparing = true;
            fragment.setSeekBarProgress(0);
            fragment.setStartTime(secondsToDuration(0));
            fragment.setSeekBarMax(0);
            fragment.setEndTime(secondsToDuration(0));
            fragment.setPlayButtonImage(false);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(track.preview_url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            showToast(R.string.error_play_song, Toast.LENGTH_SHORT);
        }
    }

    public void seekTo(int sec) {
        mediaPlayer.seekTo(sec * 1000);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPreparing = false;
        int seconds = Math.round((float) mediaPlayer.getDuration() / 1000);
        fragment.setSeekBarMax(seconds);
        fragment.setEndTime(secondsToDuration(seconds));
        play();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        fragment.setPlayButtonImage(false);
    }

    public void onDestroy() {
        handler.removeCallbacks(update);
        mediaPlayer.release();
    }

    public String secondsToDuration(int seconds) {
        return String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60), Locale.US);
    }
}
