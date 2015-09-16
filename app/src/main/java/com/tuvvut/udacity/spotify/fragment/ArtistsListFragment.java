package com.tuvvut.udacity.spotify.fragment;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;

import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.presenter.ArtistsPresenter;
import com.tuvvut.udacity.spotify.presenter.Presenter;
import com.tuvvut.udacity.spotify.util.Util;
import com.tuvvut.udacity.spotify.view.ArtistView;
import com.tuvvut.udacity.spotify.view.ViewHolder;

public class ArtistsListFragment extends MyListFragment implements SearchView.OnQueryTextListener, View.OnTouchListener {
    public static final String TAG = "ArtistFragment";
    private SearchView searchView;
    @Override
    public void onCreateView(View view) {
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        listView.setOnTouchListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBar();
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
    protected void setActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Util.setDisplayHomeAsUpEnabled(activity, false);
        Util.setTitle(activity, getString(R.string.artists_list_title));
        Util.setSubtitle(activity, "");
    }

    public void clearFocus(){
        searchView.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return ((ArtistsPresenter) presenter).onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Util.hideSoftKeyboard(getActivity());
        return false;
    }
}
