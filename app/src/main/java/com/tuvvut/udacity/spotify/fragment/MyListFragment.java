package com.tuvvut.udacity.spotify.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.tuvvut.udacity.spotify.util.Util;
import com.tuvvut.udacity.spotify.view.ViewHolder;

import java.util.List;

/**
 * Created by wu on 2015/06/14
 */
public abstract class MyListFragment<T> extends Fragment implements AdapterView.OnItemClickListener {
    protected Presenter presenter;
    protected ListView listView;
    private List<T> data;

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
        if (presenter instanceof AdapterView.OnItemClickListener) {
            ((AdapterView.OnItemClickListener) presenter).onItemClick(parent, view, position, id);
        }
    }

    public List<T> getAdapterData() {
        return data;
    }

    public void setAdapterData(List<T> objects) {
        data = objects;
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
            case android.R.id.home:
                Util.popBackStack(getFragmentManager());
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    protected abstract void setActionBar();

}
