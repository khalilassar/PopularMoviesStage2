package com.example.popularmoviesstage2;

import android.app.Application;
import android.content.res.Resources;

import com.squareup.picasso.Picasso;

public class MyApplication extends Application {
    private static Application mInstance;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        Picasso.setSingletonInstance(new Picasso.Builder(getApplicationContext()).build());
        mInstance = this;
        res = getResources();
    }

    public static Application getmInstance() {
        return mInstance;
    }

    public static Resources getRes() {
        return res;
    }
}

