package com.tuvvut.udacity.spotify.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuvvut.udacity.spotify.R;
import com.tuvvut.udacity.spotify.adapter.MyAdapter;
import com.tuvvut.udacity.spotify.presenter.Presenter;
import com.tuvvut.udacity.spotify.view.ViewHolder;

import java.util.List;

/**
 * Created by wu on 2015/06/14
 */
public abstract class MyListFragment<T> extends Fragment implements AdapterView.OnItemClickListener {
    protected Presenter presenter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        onCreateView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        presenter = getPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.onListItemClick(parent.getAdapter().getItem(position), position);
    }

    public void setAdapterData(List<T> objects) {
        MyAdapter adapter;
        if (getAdapter() == null) {
            adapter = new MyAdapter(getActivity(), objects) {
                @Override
                public int getViewHolderLayout() {
                    return getListItemLayout();
                }

                @Override
                public ViewHolder getViewHolder(View v) {
                    return getListItemViewHolder(v);
                }
            };
            listView.setAdapter(adapter);
        } else {
            adapter = (MyAdapter) getAdapter();
            adapter.setData(objects);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setTitle(String title){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public void setSubtitle(String subtitle){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    public void setDisplayHomeAsUpEnabled(boolean enable){
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enable);
        }
    }

    private Adapter getAdapter() {
        return listView.getAdapter();
    }

    public abstract void onCreateView(View view);

    public abstract int getFragmentLayout();

    public abstract int getListItemLayout();

    public abstract ViewHolder getListItemViewHolder(View v);

    public abstract Presenter getPresenter();

}
