package com.tuvvut.udacity.spotify.presenter;

import com.tuvvut.udacity.spotify.Cache;
import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.fragment.TracksListFragment;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wu on 2015/06/14
 */
public class TracksPresenter extends Presenter {
    private String subtitle = "";

    public TracksPresenter(TracksListFragment f) {
        super(f);
        List<Track> list = (List) Cache.get(Cache.TYPE.TRACKS);
        subtitle = list.get(0).artists.get(0).name;
        setAdapterData(list);
        setActionBar();
    }

    @Override
    public void onListItemClick(Object object, int position) {
        //TODO for future Stage 2
    }

    @Override
    public void setActionBar() {
        fragment.setHasOptionsMenu(true);
        fragment.setDisplayHomeAsUpEnabled(true);
        fragment.setTitle(getString(R.string.tracks_list_title));
        fragment.setSubtitle(subtitle);
    }
}
