package com.tuvvut.udacity.spotify.fragment;

import android.view.MenuItem;
import android.view.View;

import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.presenter.Presenter;
import com.tuvvut.udacity.spotify.presenter.TracksPresenter;
import com.tuvvut.udacity.spotify.util.Util;
import com.tuvvut.udacity.spotify.view.TrackView;
import com.tuvvut.udacity.spotify.view.ViewHolder;

public class TracksListFragment extends MyListFragment {
    public static final String TAG = "TracksFragment";

    @Override
    public void onCreateView(View view) {

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
        return new TracksPresenter(this);
    }
}
