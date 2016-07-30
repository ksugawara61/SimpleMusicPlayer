package org.example.simplemusicplayer.Controller;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by katsuya on 16/07/30.
 * 設定画面のアクティビティ
 */
public class SettingActivity extends AppCompatActivity {

    private static final String TAG = "SettingActivity";

    /**
     * 設定画面を生成
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        ActionBar action_bar = getSupportActionBar();
        action_bar.setTitle("設定");  // アクションバーのタイトルを「設定」に変更
        action_bar.setDisplayHomeAsUpEnabled(true);  // アクションバーの戻るボタンを有効化
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SettingFragment()).commit();
    }


    /**
     * 「戻る」がタップされた時の動作
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }


}
