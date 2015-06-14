package com.tuvvut.udacity.spotify.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tuvvut.udacity.spotify.view.ViewHolder;

import java.util.List;

/**
 * Created by wu on 2015/06/14
 */
public abstract class MyAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> data;
    private int itemLayout;

    public MyAdapter(Context context, List<T> data) {
        this.context = context;
        this.data = data;
        this.itemLayout = getViewHolderLayout();
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if (v == null) {
            v = View.inflate(context, itemLayout, null);
            holder = getViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.bind(context, getItem(position));
        return v;
    }

    public abstract int getViewHolderLayout() ;

    public abstract ViewHolder getViewHolder(View v) ;
}
