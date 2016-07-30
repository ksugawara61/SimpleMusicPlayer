package org.example.simplemusicplayer.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import org.example.simplemusicplayer.R;

/**
 * Created by katsuya on 16/07/30.
 * 設定画面のフラグメント
 */
public class SettingFragment extends PreferenceFragment {

    private static final String TAG = "SettingFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        addPreferencesFromResource(R.xml.preference);
    }

}
