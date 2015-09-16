package com.tuvvut.udacity.spotify.fragment;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tuvvut.udacity.spotify.Application;
import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.presenter.Presenter;
import com.tuvvut.udacity.spotify.presenter.TracksPresenter;
import com.tuvvut.udacity.spotify.util.Util;
import com.tuvvut.udacity.spotify.view.TrackView;
import com.tuvvut.udacity.spotify.view.ViewHolder;

public class TracksListFragment extends MyListFragment {
    public static final String TAG = "TracksFragment";
    TracksPresenter presenter;

    @Override
    public void onCreateView(View view) {
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_tracks;
    }

    @Override
    public int getListItemLayout() {
        return TrackView.LAYOUT;
    }

    @Override
    public ViewHolder getListItemViewHolder(View v) {
        return new TrackView(v);
    }

    @Override
    public Presenter getPresenter() {
        presenter = new TracksPresenter(this);
        setActionBar();
        return presenter;
    }

    protected void setActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Util.setSubtitle(activity, presenter.getSubtitle());
        if (Application.isOnePane) {
            Util.setTitle(activity, getString(R.string.tracks_list_title));
            setHasOptionsMenu(true);
            Util.setDisplayHomeAsUpEnabled(activity, true);
        }else{
            Util.setTitle(activity, getString(R.string.streamer_title));
        }
    }
}
