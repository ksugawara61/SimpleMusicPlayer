package org.example.simplemusicplayer.Controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.example.simplemusicplayer.Model.MusicDBAdapter;
import org.example.simplemusicplayer.Model.MusicMediaPlayer;
import org.example.simplemusicplayer.R;

/**
 * Created by katsuya on 16/07/21.
 */
public class MainFragment extends Fragment {

    private final static String TAG = "MainFragment";
    private AlertDialog m_dialog;
    private MusicMediaPlayer m_player;
    private boolean m_isbound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        // DBの更新
        MusicDBAdapter adapter = new MusicDBAdapter(getContext(), "simplemusicplayer.db", null, 1);
        try {
            adapter.insertMusic();
        }
        catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        doBindService();  // サービスをバインド
    }

    /**
     * ビューの生成
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        View root_view = inflater.inflate(R.layout.fragment_main, container, false);

        // 再生ボタンのイベントを作成
        View play_button = root_view.findViewById(R.id.play_button);
        play_button.setOnClickListener(btnListener);

        // 前へボタンのイベントを作成
        View prev_button = root_view.findViewById(R.id.prev_button);
        prev_button.setOnClickListener(btnListener);

        // 次へボタンのイベントを作成
        View next_button = root_view.findViewById(R.id.next_button);
        next_button.setOnClickListener(btnListener);

        // ループボタンのイベントを作成
        View roop_button = root_view.findViewById(R.id.roop_button);
        roop_button.setOnClickListener(btnListener);

        // シャッフルボタンのイベントを作成
        View shuffle_button = root_view.findViewById(R.id.shuffle_button);
        shuffle_button.setOnClickListener(btnListener);

        return root_view;
    }

    /**
     * フラグメントが破棄される時に呼び出す
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();  // サービスのアンバインド
        Log.d(TAG, "onDestroy");
    }



    /**
     * ボタンのクリックイベント
     */
    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch(view.getId()) {
                // 再生ボタン押下時の処理
                case R.id.play_button:
                    Log.i(TAG, "push play button");
                    m_player.playMusic(0, "Red", "/storage/sdcard1/music/Taylor Swift/Red/02 Red.mp3");
                    break;

                // 前へボタン押下時の処理
                case R.id.prev_button:
                    Log.i(TAG, "push prev button");
                    break;

                // 次へボタン押下時の処理
                case R.id.next_button:
                    Log.i(TAG, "push next button");
                    break;

                // ループボタン押下時の処理
                case R.id.roop_button:
                    Log.i(TAG, "push roop button");
                    break;

                // シャッフルボタン押下時の処理
                case R.id.shuffle_button:
                    Log.i(TAG, "push shuffle button");
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * サービスのコネクション関連処理
     */
    private ServiceConnection m_connection = new ServiceConnection() {
        /**
         * サービスのバインド時に呼び出す
         * @param class_name
         * @param service
         */
        public void onServiceConnected(ComponentName class_name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            m_player = ((MusicMediaPlayer.MusicBinder)service).getService();
        }

        /**
         * サービスのクラッシュ時に呼び出す
         * @param class_name
         */
        public void onServiceDisconnected(ComponentName class_name) {
            Log.i(TAG, "onServiceDisconnected");
            m_player = null;
        }
    };

    /**
     * サービスをバインド（接続）
     */
    void doBindService() {
        getActivity().bindService(new Intent(getActivity(), MusicMediaPlayer.class),
                m_connection, Context.BIND_AUTO_CREATE);
        m_isbound = true;
    }

    /**
     * サービスをアンバインド（切断）
     */
    void doUnbindService() {
        if (m_isbound) {
            // コネクションの解除
            getActivity().unbindService(m_connection);
            m_isbound = false;
        }
    }
}
