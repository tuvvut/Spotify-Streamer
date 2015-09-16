package com.tuvvut.udacity.spotify.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.tuvvut.udacity.spotify.util.Util;

/**
 * Created by wu on 2015/06/12
 */
public abstract class Presenter<T> {
    Fragment fragment;
    private Toast toast;

    public Presenter(Fragment fragment) {
        this.fragment = fragment;
    }

    public Context getContext() {
        return fragment.getActivity();
    }

    public Resources getResources() {
        return fragment.getResources();
    }

    public String getString(@StringRes int StringResId) {
        return fragment.getString(StringResId);
    }

    public void showToast(@StringRes int StringResId, int duration) {
        showToast(getString(StringResId), duration);
    }

    public void showToast(String message, int duration) {
        toast = Util.showToast(toast, getContext(), message, duration);
    }


}
