package org.example.simplemusicplayer.Controller;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.example.simplemusicplayer.Model.MusicDBAdapter;
import org.example.simplemusicplayer.R;

import static org.example.simplemusicplayer.Model.DBConstant.*;

/**
 * Created by katsuya on 16/07/30.
 * 設定画面のフラグメント
 */
public class SettingFragment extends PreferenceFragment
    implements Preference.OnPreferenceClickListener, Runnable {

    private static final String TAG = "SettingFragment";
    private ProgressDialog m_progress;
    private Thread m_thread;

    /**
     * フラグメントの生成処理
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        addPreferencesFromResource(R.xml.preference);

        // 設定メニューのクリックイベントを設定
        findPreference("folder_scan").setOnPreferenceClickListener(this);
        findPreference("folder_path").setOnPreferenceClickListener(this);
    }

    /**
     * Preferenceのクリックイベント
     * @param preference
     * @return
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.d(TAG, "onPreferenceClick");

        switch(preference.getKey()) {
            // フォルダスキャン（DBの更新処理）の実行
            case "folder_scan":
                Log.d(TAG, "click folder_scan");

                // Android 6.0対応用に外部ストレージへのアクセスが許可されているか確認
                // TODO とりあえず雑に Android 6.0に対応するように修正したのであとで直す
                Activity activity = getActivity();
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    // プログレスダイアログを表示
                    m_progress = new ProgressDialog(getActivity());
                    m_progress.setIndeterminate(true);
                    m_progress.setMessage("Loading...");
                    m_progress.setCancelable(false);  // 戻るボタンを無効化
                    m_progress.show();

                    // DBの更新処理の時間がかかるためスレッドで呼び出す
                    m_thread = new Thread(this);
                    m_thread.start();
                }
                // 許可されていない場合にダイヤログを表示
                else {
                    //まだ許可を求める前の時、許可を求めるダイアログを表示します。
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

                return true;
            // スキャンするフォルダパスの設定
            case "folder_path":
                Log.d(TAG, "click folder_path");
                return true;
            default:
                break;
        }

        return false;
    }

    /**
     * スレッド処理
     */
    @Override
    public void run() {
        Activity activity = getActivity();

        // DBの更新処理
        activity.deleteDatabase(DB_NAME);
        MusicDBAdapter adapter = new MusicDBAdapter(activity, DB_NAME, null, 1);
        adapter.insertMusic();

        // プログレスダイヤログを閉じる
        m_progress.dismiss();
    }

}
