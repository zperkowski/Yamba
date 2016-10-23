package com.zperkowski.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import winterwell.jtwitter.Twitter;

/**
 * Created by zperkowski on 30-Sep-16.
 */

public class RefreshService extends IntentService {
    static final String TAG = "RefreshService";
    Twitter twitter;

    public RefreshService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.newcircle.com/api");
        Log.d(TAG, "OnCreated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ((YambaApp)getApplication()).pullAndInsert();
        Log.d(TAG, "onHandleIntent");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroied");
    }
}
