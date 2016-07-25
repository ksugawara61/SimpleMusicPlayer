package org.example.simplemusicplayer.Controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

    // ビューの変化する箇所
    private TextView m_title_text;
    private ImageButton m_loop_button;    // ループボタン
    private ImageButton m_play_button;    // 再生ボタン
    private ImageButton m_shuffle_button; // シャッフルボタン

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
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

        // メンバ変数に格納
        m_title_text = (TextView)root_view.findViewById(R.id.title);

        // 再生ボタンのイベントを作成
        m_play_button = (ImageButton)root_view.findViewById(R.id.play_button);
        m_play_button.setOnClickListener(btnListener);

        // 前へボタンのイベントを作成
        ImageButton prev_button = (ImageButton)root_view.findViewById(R.id.prev_button);
        prev_button.setOnClickListener(btnListener);

        // 次へボタンのイベントを作成
        ImageButton next_button = (ImageButton)root_view.findViewById(R.id.next_button);
        next_button.setOnClickListener(btnListener);

        // ループボタンのイベントを作成
        m_loop_button = (ImageButton)root_view.findViewById(R.id.loop_button);
        m_loop_button.setOnClickListener(btnListener);

        // シャッフルボタンのイベントを作成
        m_shuffle_button = (ImageButton)root_view.findViewById(R.id.shuffle_button);
        m_shuffle_button.setOnClickListener(btnListener);

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
                    if (m_player.playMusic()) {
                        // 曲のタイトルを設定
                        m_title_text.setText(m_player.getMusicTitle());
                        m_play_button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                    else {
                        m_play_button.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                    break;

                // 前へボタン押下時の処理
                case R.id.prev_button:
                    Log.i(TAG, "push prev button");
                    m_player.prevMusic();

                    // 曲のタイトルを設定
                    m_title_text.setText(m_player.getMusicTitle());
                    break;

                // 次へボタン押下時の処理
                case R.id.next_button:
                    Log.i(TAG, "push next button");
                    m_player.nextMusic();

                    m_title_text.setText(m_player.getMusicTitle());
                    break;

                // ループボタン押下時の処理
                case R.id.loop_button:
                    Log.i(TAG, "push loop button");
                    if (m_player.setRoopMusic()) {
                        m_loop_button.setImageResource(R.drawable.ic_repeat_black_24dp);
                    }
                    else {
                        m_loop_button.setImageResource(R.drawable.ic_repeat_white_24dp);
                    }
                    break;

                // シャッフルボタン押下時の処理
                case R.id.shuffle_button:
                    Log.i(TAG, "push shuffle button");
                    if (m_player.setShuffleMusic()) {
                        m_shuffle_button.setImageResource(R.drawable.ic_shuffle_black_24dp);
                    }
                    else {
                        m_shuffle_button.setImageResource(R.drawable.ic_shuffle_white_24dp);
                    }
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
