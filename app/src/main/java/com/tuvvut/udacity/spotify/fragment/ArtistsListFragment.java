package com.tuvvut.udacity.spotify.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SearchView;

import com.tuvvut.udacity.spotify.Application;
import com.tuvvut.udacity.spotify.MusicService;
import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.presenter.ArtistsPresenter;
import com.tuvvut.udacity.spotify.presenter.Presenter;
import com.tuvvut.udacity.spotify.util.Util;
import com.tuvvut.udacity.spotify.view.ArtistView;
import com.tuvvut.udacity.spotify.view.ViewHolder;

public class ArtistsListFragment extends MyListFragment implements SearchView.OnQueryTextListener, View.OnTouchListener {
    public static final String TAG = "ArtistFragment";
    private SearchView searchView;
    private ArtistsPresenter presenter;

    @Override
    public void onCreateView(View view) {
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        listView.setOnTouchListener(this);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
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
        presenter = new ArtistsPresenter(this);
        return presenter;
    }

    @Override
    protected void setActionBar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Util.setDisplayHomeAsUpEnabled(activity, false);
        Util.setTitle(activity, getString(R.string.artists_list_title));
        Util.setSubtitle(activity, "");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.artistslist_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.now_playing:
                presenter.toNowPlaying();
                break;
            case R.id.country_code:
                showCountryCodeList();
                break;
            case R.id.toggle_notification:
                getActivity().startService(new Intent(MusicService.TOGGLE_NOTIFICATION, null, getActivity(), MusicService.class));
                break;
        }
        Util.hideSoftKeyboard(getActivity());
        return super.onOptionsItemSelected(item);
    }

    public void clearFocus() {
        searchView.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return presenter.onQueryTextSubmit(query);
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

    private void showCountryCodeList() {
        final String[] countries = getResources().getStringArray(R.array.country_code);
        AlertDialog.Builder listAlertDialog = new AlertDialog.Builder(getActivity());
        listAlertDialog.setTitle(R.string.country_code);
        DialogInterface.OnClickListener ListClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] countryCode = countries[which].split(",");
                Application.countryCode = countryCode[countryCode.length - 1];
            }
        };
        listAlertDialog.setItems(countries, ListClick);
        listAlertDialog.show();
    }
}
