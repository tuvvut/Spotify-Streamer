package com.tuvvut.udacity.spotify.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuvvut.udacity.spotify.Application;
import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.presenter.StreamerPresenter;
import com.tuvvut.udacity.spotify.util.Util;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wu on 2015/09/14
 */
public class StreamerFragment extends DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final String TAG = "StreamerFragment";
    private StreamerPresenter presenter;
    private TextView artistAndAlbum;
    private ImageView artwork;
    private TextView trackName;
    private SeekBar seekBar;
    private ImageButton previous;
    private ImageButton play;
    private ImageButton next;
    private TextView startTime;
    private TextView endTime;

    public void setTracks(List<Track> tracks, int index) {
        presenter = new StreamerPresenter(this, tracks, index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        presenter.showTrackInfo();
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
        presenter.onDestroy();
        super.onDestroy();
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

    public void setTrackInfo(Track track) {
        artistAndAlbum.setText(track.artists.get(0).name + "\n" + track.album.name);
        trackName.setText(track.name);
        if (track.album.images.size() > 0) {
            int index = 0;
            Picasso.with(getActivity()).load(track.album.images.get(index).url).into(artwork);
        } else {
            Picasso.with(getActivity()).load(R.mipmap.no_image).into(artwork);
        }
    }

    public void setStartTime(String time) {
        startTime.setText(time);
    }

    public void setEndTime(String time) {
        endTime.setText(time);
    }

    public void setSeekBarMax(int max) {
        seekBar.setMax(max);
    }

    public void setSeekBarProgress(int progress) {
        seekBar.setProgress(progress);
    }

    public void setPreviousButton(boolean enabled) {
        previous.setEnabled(enabled);
    }

    public void setNextButton(boolean enabled) {
        next.setEnabled(enabled);
    }

    public void setPlayButtonImage(boolean isPlaying) {
        play.setImageResource(isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous:
                presenter.previous();
                break;
            case R.id.play:
                presenter.play();
                break;
            case R.id.next:
                presenter.next();
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
            setStartTime(presenter.secondsToDuration(progress));
            presenter.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
