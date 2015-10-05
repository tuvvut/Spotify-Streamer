package com.tuvvut.udacity.spotify.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tuvvut.udacity.spotify.Application;
import com.tuvvut.udacity.spotify.MusicController;
import com.tuvvut.udacity.spotify.MusicService;
import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.util.Util;

import java.util.List;
import java.util.Locale;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wu on 2015/09/14
 */
public class StreamerFragment extends DialogFragment implements MusicController.MusicControlListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final String TAG = "StreamerFragment";
    private TextView artistAndAlbum;
    private ImageView artwork;
    private TextView trackName;
    private SeekBar seekBar;
    private ImageButton previous;
    private ImageButton play;
    private ImageButton next;
    private TextView startTime;
    private TextView endTime;
    private ShareActionProvider shareActionProvider;
    private String shareText = "";
    private MusicController musicController = MusicController.getInstance();
    private Toast toast = null;
    private boolean needToResetMusicController = false;
    private boolean cancel = false;
    private boolean isFragmentVisible = false;
    private Handler handler = new Handler();
    private Runnable update = new Runnable() {
        @Override
        public void run() {
            setCurrentPosition();
            if (musicController.isPlaying()) {
                handler.postDelayed(update, 200);
            }
        }
    };

    public void setTracks(List<Track> tracks, int index) {
        musicController.setSongIndex(index);
        musicController.setSongsList(tracks);
        needToResetMusicController = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().startService(new Intent(getActivity(), MusicService.class));
        View v = inflater.inflate(R.layout.fragment_streamer, container, false);
        artistAndAlbum = (TextView) v.findViewById(R.id.artistAndAlbum);
        artwork = (ImageView) v.findViewById(R.id.artwork);
        trackName = (TextView) v.findViewById(R.id.trackName);
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        previous = (ImageButton) v.findViewById(R.id.previous);
        play = (ImageButton) v.findViewById(R.id.play);
        next = (ImageButton) v.findViewById(R.id.next);
        startTime = (TextView) v.findViewById(R.id.startTime);
        endTime = (TextView) v.findViewById(R.id.endTime);

        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        musicController.register(this);
        if (needToResetMusicController) {
            musicController.reset();
        } else {
            setViews();
        }
        if (!musicController.isPreparing()) {
            setCurrentPosition();
            setDuration();
            if (musicController.isPlaying()) {
                play();
            }
        }
        setActionBar();
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(update);
        musicController.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.streamer_fragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (!TextUtils.isEmpty(shareText)) {
            shareActionProvider.setShareIntent(createShareTrackIntent());
        }
    }

    private Intent createShareTrackIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        return shareIntent;
    }

    private void setActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Util.setTitle(activity, getString(R.string.streamer_title));
        if (Application.isOnePane) {
            Util.setSubtitle(activity, "");
            setHasOptionsMenu(true);
            Util.setDisplayHomeAsUpEnabled(activity, true);
        }
    }

    private void setViews() {
        Track track = musicController.getTrackForNow();
        shareText = track.artists.get(0).name + "\n" + track.album.name + "\n" + track.name + "\n" + track.external_urls.get("spotify");
        artistAndAlbum.setText(track.artists.get(0).name + "\n" + track.album.name);
        trackName.setText(track.name);
        if (track.album.images.size() > 0) {
            Picasso.with(getActivity()).load(track.album.images.get(0).url).into(artwork);
        } else {
            Picasso.with(getActivity()).load(R.mipmap.no_image).into(artwork);
        }
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareTrackIntent());
        }

        setPlayButtonImage(false);
        previous.setEnabled(musicController.isMorePrevious());
        next.setEnabled(musicController.isMoreNext());
        seekBar.setProgress(0);
        seekBar.setMax(0);
        startTime.setText(secondsToDuration(0));
        endTime.setText(secondsToDuration(0));
    }

    public void setPlayButtonImage(boolean isPlaying) {
        play.setImageResource(isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }

    private void setDuration() {
        int maxSeconds = Math.round((float) musicController.getDuration() / 1000);
        seekBar.setMax(maxSeconds);
        endTime.setText(secondsToDuration(maxSeconds));
    }

    private void setCurrentPosition() {
        int seconds = Math.round((float) (musicController.getCurrentPosition()) / 1000);
        seekBar.setProgress(seconds);
        startTime.setText(secondsToDuration(seconds));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                musicController.previous();
                break;
            case R.id.play:
                musicController.playOrPause();
                break;
            case R.id.next:
                musicController.next();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Util.popBackStack(getFragmentManager());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            startTime.setText(secondsToDuration(progress));
            musicController.seekTo(progress * 1000);
        }
    }

    @Override
    public void reset() {
        setViews();
    }

    @Override
    public void ready() {
        setDuration();
    }

    @Override
    public void play() {
        setPlayButtonImage(true);
        handler.postDelayed(update, 200);
    }

    @Override
    public void pause() {
        handler.removeCallbacks(update);
        setPlayButtonImage(false);
    }

    @Override
    public void stop() {
        handler.removeCallbacks(update);
        setPlayButtonImage(false);
    }

    @Override
    public void cancel() {
        if (isFragmentVisible){
            handler.removeCallbacks(update);
            boolean mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
            if (!mIsLargeLayout) {
                this.dismiss();
            } else {
                Util.popBackStack(getFragmentManager());
            }
            cancel = false;
        }else{
            cancel = true;
        }
    }

    @Override
    public void onPause() {
        isFragmentVisible = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        isFragmentVisible = true;
        if (cancel){
            cancel();
        }
    }

    @Override
    public void error(int errorMessageResId) {
        Util.showToast(toast, getActivity(), getString(errorMessageResId), Toast.LENGTH_SHORT);
    }

    public String secondsToDuration(int seconds) {
        return String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60), Locale.US);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
