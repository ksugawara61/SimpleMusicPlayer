package org.example.simplemusicplayer.Model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by katsuya on 16/07/24.
 */
public class MusicMediaPlayer extends Service
        implements MediaPlayer.OnCompletionListener
{

    /**
     * サービスに接続するためのバインダーの生成
     */
    public class MusicBinder extends Binder {
        //サービスの取得
        public MusicMediaPlayer getService() {
            return MusicMediaPlayer.this;
        }
    }


    private static final String TAG = "MusicMediaPlayer";
    private final IBinder m_binder = new MusicBinder();  // Binderの生成
    private MediaPlayer m_player = null;  // 音楽プレーヤー
    private int m_id;


    /**
     * サービスのバインド開始時に呼び出す
     */
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
    }

    /**
     * サービスのバインド時 onCreate()の後に呼ばれる
     * @param intent	インテント
     * @return m_binder	生成したバインダー
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: " + intent);
        return m_binder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int start_id) {
        Log.i(TAG, "onStartCommand Received start id " + start_id + ": " + intent);
        //明示的にサービスの起動、停止が決められる場合の返り値
        return START_STICKY;
    }

    /**
     *
     */
    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: " + intent);
    }

    /**
     * サービスのアンバインド後に呼び出す
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: " + intent);
        return true;
    }

    /**
     * サービスのアンバインド後、onUnbind()の処理が終了すると呼び出す
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        stopMusic();  // 音楽の停止
    }


    /**
     * 音楽再生終了時の処理
     * @param player 音楽プレーヤー
     */
    public void onCompletion(MediaPlayer player) {
        Log.i(TAG, "onCompletion");
    }

    /**
     * 音楽の再生
     */
    public void playMusic(int id, String title, String path) {
        Log.i(TAG, "playMusic");

        m_id = id;

        // 音楽プレーヤーが再生中の場合一時停止する
        if (m_player != null) {
            if (m_player.isPlaying()) {
                pauseMusic();
                return;
            }
            else {
                unpauseMusic();
                return;
            }
        }

        // 音楽を再生
        m_player = new MediaPlayer();
        try {
            m_player.setDataSource(path);
            m_player.prepare();
            m_player.start();
            m_player.setOnCompletionListener(this);
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 音楽の一時停止
     */
    public void pauseMusic() {
        Log.i(TAG, "pauseMusic");
        if (m_player != null) {
            m_player.pause();
        }
    }

    /**
     * 音楽の再開
     */
    public void unpauseMusic() {
        Log.i(TAG, "unpauseMusic");
        if (m_player != null) {
            m_player.start();
        }
    }

    /**
     * 音楽の停止
     */
    public void stopMusic() {
        Log.i(TAG, "stopMusic");
        if (m_player != null) {
            m_player.stop();
            m_player.setOnCompletionListener(null);
            m_player.release();
            m_player = null;
        }
    }
}
