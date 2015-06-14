package com.tuvvut.udacity.spotify.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by wu on 2015/06/14
 */
public abstract class MyAsyncTask<Params> {
    private Context context;

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    public void execute(Params query) {
        new AsyncTask<Params, Void, Object>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = Util.getProgressDialog(context);
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Params... params) {
                try {
                    return MyAsyncTask.this.doInBackground(params[0]);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Object object) {
                if (object == null) {
                    MyAsyncTask.this.onDoInBackgroundError();
                }else{
                    MyAsyncTask.this.onPostExecute(object);
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                super.onPostExecute(object);
            }
        }.execute(query);
    }

    public abstract Object doInBackground(Params params);

    public abstract void onPostExecute(Object object);

    public abstract void onDoInBackgroundError();
}
