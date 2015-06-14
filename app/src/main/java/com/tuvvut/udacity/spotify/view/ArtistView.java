package com.tuvvut.udacity.spotify.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuvvut.udacity.spotify.R;

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by wu on 2015/06/14
 */
public class ArtistView implements ViewHolder<Artist> {
    public static final int LAYOUT = R.layout.list_item;
    private ImageView image;
    private TextView text;

    public ArtistView(View v) {
        image = (ImageView) v.findViewById(R.id.image);
        text = (TextView) v.findViewById(R.id.text);
    }

    @Override
    public void bind(Context context, Artist artist) {
        text.setText(artist.name);
        if (artist.images.size() > 0) {
            int index = 0;
            Picasso.with(context).load(artist.images.get(index).url).into(image);
        } else {
            Picasso.with(context).load(R.mipmap.no_image).into(image);
        }
    }
}
