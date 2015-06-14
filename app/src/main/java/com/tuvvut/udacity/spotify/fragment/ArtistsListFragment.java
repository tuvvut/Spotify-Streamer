package com.tuvvut.udacity.spotify.fragment;

import android.view.View;
import android.widget.SearchView;

import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.presenter.ArtistsPresenter;
import com.tuvvut.udacity.spotify.presenter.Presenter;
import com.tuvvut.udacity.spotify.view.ArtistView;
import com.tuvvut.udacity.spotify.view.ViewHolder;

public class ArtistsListFragment extends MyListFragment implements SearchView.OnQueryTextListener {
    public static final String TAG = "ArtistFragment";

    @Override
    public void onCreateView(View view) {
        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setActionBar();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_artists;
    }

    @Override
    public int getListItemLayout() {
        return ArtistView.LAYOUT;
    }

    @Override
    public ViewHolder getListItemViewHolder(View v) {
        return new ArtistView(v);
    }

    @Override
    public Presenter getPresenter() {
        return new ArtistsPresenter(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return ((ArtistsPresenter) presenter).onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
