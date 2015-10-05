package com.tuvvut.udacity.spotify;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.os.IBinder;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tuvvut.udacity.spotify.util.Util;

import kaaes.spotify.webapi.android.models.Track;

import static com.tuvvut.udacity.spotify.MusicController.CANCEL;
import static com.tuvvut.udacity.spotify.MusicController.NEXT;
import static com.tuvvut.udacity.spotify.MusicController.PLAY_OR_PAUSE;
import static com.tuvvut.udacity.spotify.MusicController.PREVIOUS;
import static com.tuvvut.udacity.spotify.MusicController.STOP;
import static com.tuvvut.udacity.spotify.MusicController.getInstance;

/**
 * Created by wu on 2015/09/21
 */
public class MusicService extends Service implements MusicController.MusicControlListener, AudioManager.OnAudioFocusChangeListener {
    public final static String TOGGLE_NOTIFICATION = "TOGGLE_NOTIFICATION";
    private final int NOTIFICATION_ID = 1234;
    private MusicController musicController = getInstance();
    private Notification notification;
    private RemoteControlClient remoteControlClient;
    private AudioManager audioManager;
    private Toast toast;
    private boolean showNotification = true;
    private int playbackState = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicController.register(this);
        notification = new NotificationCompat.Builder(this) //
                .setSmallIcon(R.mipmap.ic_launcher) //
                .setTicker("Spotify Streamer") //
                .setContentTitle("Spotify Streamer") //
                .build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.contentView = new RemoteViews(getPackageName(), R.layout.notification_streamer);
        notification.bigContentView = new RemoteViews(getPackageName(), R.layout.notification_streamer_big);
        setRemoteViewListener(notification.contentView);
        setRemoteViewListener(notification.bigContentView);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        ComponentName remoteComponentName = new ComponentName(getApplicationContext(), MusicReceiver.class.getName());
        mediaButtonIntent.setComponent(remoteComponentName);
        PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);
        remoteControlClient = new RemoteControlClient(mediaPendingIntent);
        int flags = RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS | RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_NEXT;
        remoteControlClient.setTransportControlFlags(flags);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.registerRemoteControlClient(remoteControlClient);
        audioManager.registerMediaButtonEventReceiver(remoteComponentName);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            switch (intent.getAction()) {
                case PLAY_OR_PAUSE:
                    musicController.playOrPause();
                    break;
                case STOP:
                    musicController.stop();
                    break;
                case NEXT:
                    musicController.next();
                    break;
                case PREVIOUS:
                    musicController.previous();
                    break;
                case CANCEL:
                    musicController.cancel();
                    break;
                case TOGGLE_NOTIFICATION:
                    toggleNotification();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(Track track) {
        if (showNotification && track != null) {
            UpdateRemoteControlClient(track);
            updateRemoteView(notification.contentView, notification, track);
            updateRemoteView(notification.bigContentView, notification, track);
            startForeground(NOTIFICATION_ID, notification);
        }
    }

    private void toggleNotification() {
        showNotification = !showNotification;
        if (showNotification) {
            showNotification(musicController.getTrackForNow());
        } else {
            remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_STOPPED);
            stopForeground(true);
        }
    }

    private void updateRemoteView(RemoteViews remoteView, Notification notification, Track track) {
        if (track.album.images.size() > 0) {
            Picasso.with(this).load(track.album.images.get(0).url).into(remoteView, R.id.artwork, NOTIFICATION_ID, notification);
        } else {
            Picasso.with(this).load(R.mipmap.no_image).into(remoteView, R.id.artwork, NOTIFICATION_ID, notification);
        }
        remoteView.setTextViewText(R.id.artistAndAlbum, track.artists.get(0).name + "\n" + track.album.name);
        remoteView.setTextViewText(R.id.trackName, track.name);
        remoteView.setBoolean(R.id.previous, "setEnabled", musicController.isMorePrevious());
        remoteView.setBoolean(R.id.next, "setEnabled", musicController.isMoreNext());
        remoteView.setImageViewResource(R.id.play, musicController.isPlaying() ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }

    private void UpdateRemoteControlClient(Track track) {
        if (remoteControlClient == null) {
            return;
        }
        remoteControlClient.setPlaybackState(playbackState);
        RemoteControlClient.MetadataEditor metadataEditor = remoteControlClient.editMetadata(true);
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ALBUM, track.album.name);
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, track.artists.get(0).name);
        metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, track.name);
        metadataEditor.apply();
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    private void setRemoteViewListener(RemoteViews remoteView) {
        PendingIntent playOrPause = PendingIntent.getService(getApplicationContext(), 0, new Intent(PLAY_OR_PAUSE, null, this, MusicService.class),
                                                             PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.play, playOrPause);

        PendingIntent next = PendingIntent.getService(getApplicationContext(), 0, new Intent(NEXT, null, this, MusicService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.next, next);

        PendingIntent previous = PendingIntent.getService(getApplicationContext(), 0, new Intent(PREVIOUS, null, this, MusicService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.previous, previous);

        PendingIntent cancel = PendingIntent.getService(getApplicationContext(), 0, new Intent(CANCEL, null, this, MusicService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.cancel, cancel);
    }

    @Override
    public void reset() {
        showNotification(musicController.getTrackForNow());
    }

    @Override
    public void ready() {

    }

    @Override
    public void play() {
        playbackState = RemoteControlClient.PLAYSTATE_PLAYING;
        showNotification(musicController.getTrackForNow());
    }

    @Override
    public void pause() {
        playbackState = RemoteControlClient.PLAYSTATE_PAUSED;
        showNotification(musicController.getTrackForNow());
    }

    @Override
    public void stop() {
        playbackState = RemoteControlClient.PLAYSTATE_STOPPED;
        showNotification(musicController.getTrackForNow());
    }

    @Override
    public void cancel() {
        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        musicController.onDestroy();
        musicController.unregister(this);
        super.onDestroy();
    }

    @Override
    public void error(@StringRes int errorMessageResId) {
        Util.showToast(toast, this, getString(errorMessageResId), Toast.LENGTH_SHORT);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}
