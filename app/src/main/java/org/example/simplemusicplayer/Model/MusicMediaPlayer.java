package org.example.simplemusicplayer.Model;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by katsuya on 16/07/24.
 */
public class MusicMediaPlayer extends Service
{
    private static final String TAG = "MusicMediaPlayer";

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        Toast.makeText(this, "MyService#onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand Received start id " + startId + ": " + intent);
        Toast.makeText(this, "MyService#onStartCommand", Toast.LENGTH_SHORT).show();
        //明示的にサービスの起動、停止が決められる場合の返り値
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        Toast.makeText(this, "MyService#onDestroy", Toast.LENGTH_SHORT).show();
    }

    /**
     * サービスに接続するためのバインダーの生成
     */
    public class MusicBinder extends Binder {
        //サービスの取得
        MusicMediaPlayer getService() {
            return MusicMediaPlayer.this;
        }
    }

    // Binderの生成
    private final IBinder m_binder = new MusicBinder();

    /**
     * サービスへの通信チャンネルを戻す
     *
     * @param intent	インテント
     * @return m_binder	生成したバインダー
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: " + intent);
        return m_binder;
    }

    /**
     *
     */
    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind: " + intent);
    }

    /**
     *
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: " + intent);
        return true;
    }

    /**
     *
     */
    public void onCompletion(MediaPlayer player) {

    }

    /**
     * 音楽の再生
     */
    /*public void playMusic(int id, String title, String path) {
        m_id = id;

        // プレーヤーに情報が残っている場合停止する
        if (m_player != null) {
            m_player.stop();
            m_player.release();
        }

        // 音楽ファイル再生用にURIを生成
        Uri.Builder builder = new Uri.Builder();
        builder.path(path);
        builder.scheme("file");

        m_player = MediaPlayer.create(this, builder.build());

        // 音楽を再生
        m_player.start();
        m_player.setOnCompletionListener(this);
    }*/

    /**
     * 音楽の一時停止
     */
    /*public void pauseMusic() {
        if (m_player != null) {
            m_player.pause();
        }
    }*/

    /**
     * 音楽の再開
     */
    /*public void unpauseMusic() {
        if (m_player != null) {
            m_player.start();
        }
    }*/

    /**
     * 音楽の停止
     */
    /*public void stopMusic() {
        if (m_player != null) {
            m_player.stop();
            m_player.setOnCompletionListener(null);
            m_player.release();
            m_player = null;
        }
    }*/

    /*private MediaPlayer m_player;
    private final IBinder m_binder = new MPBinder();
    private int m_id;
    private int m_music_len;

    @Override
    public void onStart(Intent intent, int start_id) {

    }

    @Override
    public void onDestroy() {
        // サウンドの停止
    }*/

}
