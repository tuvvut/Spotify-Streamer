package com.tuvvut.udacity.spotify.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tuvvut.udacity.spotify.R;

/**
 * Created by wu on 2015/06/14
 */
public class Util {

    public static void setTitle(AppCompatActivity activity, String title) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public static void setSubtitle(AppCompatActivity activity, String subtitle) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    public static void setDisplayHomeAsUpEnabled(AppCompatActivity activity, boolean enable) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }

    public static void replace(FragmentManager fragmentManager, int layout, Fragment f, String tag, boolean addToBackStack) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.replace(layout, f, tag);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public static void popBackStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getString(R.string.loading));
        return progressDialog;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static Toast showToast(Toast toast, Context context, String string, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, string, duration);
        } else {
            toast.setText(string);
            toast.setDuration(duration);
        }
        toast.show();
        return toast;
    }
}
