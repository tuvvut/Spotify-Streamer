package com.tuvvut.udacity.spotify.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuvvut.udacity.spotify.R;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by wu on 2015/06/14
 */
public class TrackView implements ViewHolder<Track>{
    public static final int LAYOUT = R.layout.list_item;
    private ImageView image;
    private TextView text;

    public TrackView(View v) {
        image = (ImageView) v.findViewById(R.id.image);
        text = (TextView) v.findViewById(R.id.text);
    }

    @Override
    public void bind(Context context, Track track) {
        text.setText(track.name + "\n" + track.album.name);
        if (track.album.images.size() > 0) {
            Picasso.with(context).load(track.album.images.get(0).url).into(image);
        } else {
            Picasso.with(context).load(R.mipmap.no_image).into(image);
        }
    }
}
