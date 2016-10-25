package com.zperkowski.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by zperkowski on 06-Oct-16.
 */

public class RefreshScheduleReceiver extends BroadcastReceiver {
    static final String TAG = "RefreshScheduleReceiver";

    static PendingIntent lastOp;

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        long interval = Long.parseLong(prefs.getString("DELAY", "900000"));

        PendingIntent operation = PendingIntent.getService(context, -1,
                new Intent(YambaApp.ACTION_REFRESH), PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(lastOp);

        if (interval > 0)
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, System.currentTimeMillis(),
                interval, operation);

        lastOp = operation;

        Log.d(TAG, "onReceive: " + interval);
    }
}
