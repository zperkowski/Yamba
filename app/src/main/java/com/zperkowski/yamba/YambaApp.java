package com.zperkowski.yamba;

import android.app.Application;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by zperkowski on 30-Sep-16.
 */

public class YambaApp extends Application {
    static final String TAG = "YambaApp";
    private Twitter twitter;

    @Override
    public void onCreate() {
        super.onCreate();
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.newcircle.com/api");
        Log.d(TAG, "onCreated");
    }

    public Twitter getTwitter() {
        return twitter;
    }
}
