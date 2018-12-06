package com.example.a9onhud.theemotionaldiary;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static Context mContext;

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //  instance = this;
        mContext = getApplicationContext();
    }
}