package com.zperkowski.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zperkowski on 06-Oct-16.
 */

public class BootReceiver extends BroadcastReceiver {
    static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, UpdaterService.class));
        Log.d(TAG, "onReceive");
    }
}
