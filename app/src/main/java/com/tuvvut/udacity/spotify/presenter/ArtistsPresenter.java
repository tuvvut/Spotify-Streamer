package com.tuvvut.udacity.spotify.presenter;

import android.widget.Toast;

import com.tuvvut.udacity.spotify.Cache;
import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.fragment.ArtistsListFragment;
import com.tuvvut.udacity.spotify.fragment.TracksListFragment;
import com.tuvvut.udacity.spotify.util.MyAsyncTask;
import com.tuvvut.udacity.spotify.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created by wu on 2015/06/14
 */
public class ArtistsPresenter extends Presenter {
    private SpotifyService spotify;
    private MyAsyncTask searchArtists;
    private MyAsyncTask getArtistTopTrack;

    public ArtistsPresenter(ArtistsListFragment f) {
        super(f);
        this.spotify = new SpotifyApi().getService();
        this.searchArtists = new MyAsyncTask<String>(getContext()) {
            @Override
            public ArtistsPager doInBackground(String query) {
                return spotify.searchArtists(query);
            }

            @Override
            public void onDoInBackgroundError() {
                showToast(getString(R.string.something_wrong), Toast.LENGTH_SHORT);
            }

            @Override
            public void onPostExecute(Object object) {
                ArtistsPager artistsPager = (ArtistsPager) object;
                if (artistsPager.artists.items.size() == 0) {
                    showToast(getString(R.string.noResult), Toast.LENGTH_SHORT);
                } else {
                    Cache.put(Cache.TYPE.ARTISTS, artistsPager.artists.items);
                    setAdapterData(artistsPager.artists.items);
                }
            }
        };

        this.getArtistTopTrack = new MyAsyncTask<String>(getContext()) {
            @Override
            public Tracks doInBackground(String query) {
                Map<String, Object> values = new HashMap<>();
                values.put("country", "SE");
                return spotify.getArtistTopTrack(query, values);
            }

            @Override
            public void onDoInBackgroundError() {
                showToast(getString(R.string.something_wrong), Toast.LENGTH_SHORT);
            }

            @Override
            public void onPostExecute(Object object) {
                Tracks tracks = (Tracks) object;
                if (tracks.tracks.size() == 0) {
                    showToast(getString(R.string.noResult), Toast.LENGTH_SHORT);
                } else {
                    Cache.put(Cache.TYPE.TRACKS, tracks.tracks);
                    Util.replace(fragment.getFragmentManager(), R.id.container, new TracksListFragment(), TracksListFragment.TAG, true);
                }
            }
        };

        Object data = Cache.get(Cache.TYPE.ARTISTS);
        if (data != null) {
            setAdapterData((List) data);
        }
    }

    @Override
    public void setActionBar(){
        fragment.setDisplayHomeAsUpEnabled(false);
        fragment.setTitle(getString(R.string.artists_list_title));
        fragment.setSubtitle("");
    }

    public boolean onQueryTextSubmit(String query) {
        searchArtists.execute(query);
        return true;
    }

    @Override
    public void onListItemClick(Object object, int position) {
        getArtistTopTrack.execute(((Artist) object).id);
    }
}
