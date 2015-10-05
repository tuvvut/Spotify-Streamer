package com.tuvvut.udacity.spotify.presenter;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;

import com.tuvvut.udacity.spotify.Application;
import com.tuvvut.udacity.spotify.Cache;
import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.fragment.StreamerFragment;
import com.tuvvut.udacity.spotify.fragment.TracksListFragment;
import com.tuvvut.udacity.spotify.util.Util;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wu on 2015/06/14
 */
public class TracksPresenter extends Presenter implements AdapterView.OnItemClickListener {
    private String subtitle = "";
    private TracksListFragment fragment;
    private boolean mIsLargeLayout;

    public TracksPresenter(TracksListFragment f) {
        super(f);
        List<Track> list = (List) Cache.get(Cache.TYPE.TRACKS);
        subtitle = list.get(0).artists.get(0).name;
        fragment = f;
        fragment.setAdapterData(list);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);
    }

    public String getSubtitle() {
        return subtitle;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showStreamerUI(position);
    }

    public void showStreamerUI(int index) {
        FragmentManager fragmentManager = fragment.getFragmentManager();
        StreamerFragment streamer = new StreamerFragment();
        streamer.setTracks(fragment.getAdapterData(), index);
        if (!mIsLargeLayout) {
            streamer.show(fragmentManager, StreamerFragment.TAG);
        } else {
            Util.replace(fragmentManager, R.id.container, streamer, StreamerFragment.TAG, Application.isOnePane);
        }
    }
}
