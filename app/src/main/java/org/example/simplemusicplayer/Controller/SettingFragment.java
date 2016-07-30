package org.example.simplemusicplayer.Controller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
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

                // プログレスダイアログを表示
                m_progress = new ProgressDialog(getActivity());
                m_progress.setIndeterminate(true);
                m_progress.setMessage("Loading...");
                m_progress.setCancelable(false);  // 戻るボタンを無効化
                m_progress.show();

                // DBの更新処理の時間がかかるためスレッドで呼び出す
                m_thread = new Thread(this);
                m_thread.start();

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
        // DBの更新処理
        getActivity().deleteDatabase(DB_NAME);
        MusicDBAdapter adapter = new MusicDBAdapter(getActivity(), DB_NAME, null, 1);
        adapter.insertMusic();

        // プログレスダイヤログを閉じる
        m_progress.dismiss();
    }

}
