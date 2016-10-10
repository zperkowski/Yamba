package com.zperkowski.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

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
        StatusData statusData = ((YambaApp)getApplication()).statusData;
        try {
            List<Twitter.Status> timeline = twitter.getPublicTimeline();

            for (Twitter.Status status : timeline) {
                statusData.insert(status);
                Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
            }
        } catch (TwitterException e) {
            Log.e(TAG, "Failed to access twitter service", e);
        }
        Log.d(TAG, "onHandleIntent");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroied");
    }
}
