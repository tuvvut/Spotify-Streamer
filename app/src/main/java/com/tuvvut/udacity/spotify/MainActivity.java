package com.tuvvut.udacity.spotify;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tuvvut.udacity.spotify.fragment.ArtistsListFragment;
import com.tuvvut.udacity.spotify.util.Util;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cache.clear();
        setContentView(R.layout.activity_main);
        Application.isOnePane = findViewById(R.id.artistsListFragment) == null;
        if (Application.isOnePane) {
            if (savedInstanceState == null) {
                Util.replace(getSupportFragmentManager(), R.id.container, new ArtistsListFragment(), ArtistsListFragment.TAG, false);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
