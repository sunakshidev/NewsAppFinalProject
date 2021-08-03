package com.bbcnewsreader.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class App extends Application {

    public static App mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}
