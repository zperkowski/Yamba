package com.zperkowski.yamba;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by zperkowski on 05-Oct-16.
 */

public class PrefsActivity extends PreferenceActivity {
    static final String TAG = "PrefsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
