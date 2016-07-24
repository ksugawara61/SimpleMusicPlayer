package org.example.simplemusicplayer.Model;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by katsuya on 16/07/24.
 * 音楽関連の処理を行うクラス
 * @version 1.0
 * @author katsuya
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
    private MusicDBAdapter m_adapter = null;
    private int m_id;
    private Cursor m_cursor;
    private String m_title;
    private String m_path;

    /**
     * サービスのバインド開始時に呼び出す
     */
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");

        // DBの更新
        m_adapter = new MusicDBAdapter(this, "simplemusicplayer.db", null, 1);
        m_adapter.insertMusic();

        // 音楽ファイルの先頭のレコードを初期値として設定
        m_cursor = m_adapter.rawQueryMusic("select music_id, music_name, music_path from music", null);
        m_cursor.moveToFirst();
        m_id = Integer.parseInt(m_cursor.getString(0));
        m_title = m_cursor.getString(1);
        m_path = m_cursor.getString(2);

        Log.i(TAG, m_title);
        Log.i(TAG, m_path);
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
    public void playMusic() {
        Log.i(TAG, "playMusic");

        if (m_player != null) {
            // 音楽プレーヤーが再生中の場合一時停止
            if (m_player.isPlaying()) {
                pauseMusic();
                return;
            }
            // 音楽プレーヤーが一時停止中の場合曲を再開
            else {
                unpauseMusic();
                return;
            }
        }
        createMusic();
    }

    /**
     * 前の曲を再生
     */
    public void prevMusic() {
        Log.i(TAG, "prevMusic");
        if(!m_cursor.moveToPrevious()) {
            m_cursor.moveToLast();
        }
        m_id = Integer.parseInt(m_cursor.getString(0));
        m_title = m_cursor.getString(1);
        m_path = m_cursor.getString(2);

        Log.i(TAG, m_title);
        Log.i(TAG, m_path);

        createMusic();
    }

    /**
     * 次の曲を再生
     */
    public void nextMusic() {
        Log.i(TAG, "nextMusic");
        if(!m_cursor.moveToNext()) {
            m_cursor.moveToFirst();
        }
        m_id = Integer.parseInt(m_cursor.getString(0));
        m_title = m_cursor.getString(1);
        m_path = m_cursor.getString(2);

        Log.i(TAG, m_title);
        Log.i(TAG, m_path);

        createMusic();
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
            if(m_player.isPlaying()) {
                m_player.stop();
            }
            m_player.setOnCompletionListener(null);
            m_player.release();
            m_player = null;
        }
    }

    /**
     * 音楽のインスタンスを生成し曲を再生する
     */
    private void createMusic() {
        //前の音楽情報が残っている場合削除
        if (m_player != null) {
            m_player.stop();
            m_player.release();
        }

        m_player = new MediaPlayer();
        try {
            m_player.setDataSource(m_path);
            m_player.prepare();
            m_player.start();
            m_player.setOnCompletionListener(this);
        }
        catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }
}
