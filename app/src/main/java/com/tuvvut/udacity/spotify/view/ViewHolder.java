package com.tuvvut.udacity.spotify.view;

import android.content.Context;

/**
 * Created by wu on 2015/06/14
 */
public interface ViewHolder<T> {
    void bind(Context context, T object);
}
