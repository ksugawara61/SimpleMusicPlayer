package org.example.simplemusicplayer.Model;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

/**
 * Created by katsuya on 16/07/24.
 * 音楽関連の処理を行うクラス
 * @version 1.0
 * @author katsuya
 */
public class MusicService extends Service
        implements MediaPlayer.OnCompletionListener
{

    /**
     * サービスに接続するためのバインダーの生成
     */
    public class MusicBinder extends Binder {
        //サービスの取得
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private static final String TAG = "MusicMediaPlayer";
    private final IBinder m_binder = new MusicBinder();  // Binderの生成
    private MediaPlayer m_player = null;  // 音楽プレーヤー
    private MusicDBAdapter m_adapter = null;
    private int m_musiclen;  // レコード数
    private Random m_rand = new Random();   // シャッフル用の乱数
    private int m_id;
    private Cursor m_cursor;
    private String m_title;
    private String m_path;
    private boolean m_roopflag = false; // ループフラグ
    private boolean m_shuffleflag = false;  // シャッフルフラグ

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
        m_musiclen = m_cursor.getCount();  // レコード数を取得
        m_cursor.moveToFirst();
        m_id = Integer.parseInt(m_cursor.getString(0));
        m_title = m_cursor.getString(1);
        m_path = m_cursor.getString(2);

        Log.i(TAG, m_cursor.getString(0));
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

        // ループフラグがtrueのとき同じ曲を連続して再生
        if (m_roopflag) {
            createMusic();
        }
        // ループフラグがfalseのとき次の曲を再生
        else {
            nextMusic();
        }
    }

    /**
     * 再生している音楽のタイトルを取得
     * @return m_title 音楽のタイトル
     */
    public String getMusicTitle() {
        return m_title;
    }

    /**
     * 音楽の再生
     * @return m_player.isPlaying()  音楽再生のON・OFF
     */
    public boolean playMusic() {
        Log.i(TAG, "playMusic");

        if (m_player != null) {
            // 音楽プレーヤーが再生中の場合一時停止
            if (m_player.isPlaying()) {
                pauseMusic();
                return m_player.isPlaying();
            }
            // 音楽プレーヤーが一時停止中の場合曲を再開
            else {
                unpauseMusic();
                return m_player.isPlaying();
            }
        }
        createMusic();
        return m_player.isPlaying();
    }

    /**
     * 前の曲を再生
     */
    public void prevMusic() {
        Log.i(TAG, "prevMusic");

        // シャッフルがONの場合次の音楽をランダムで指定
        if (m_shuffleflag) {
            shuffleMusic();
            return;
        }

        if(!m_cursor.moveToPrevious()) {
            m_cursor.moveToLast();
        }
        m_id = Integer.parseInt(m_cursor.getString(0));
        m_title = m_cursor.getString(1);
        m_path = m_cursor.getString(2);

        Log.i(TAG, m_cursor.getString(0));
        Log.i(TAG, m_title);
        Log.i(TAG, m_path);

        createMusic();
    }

    /**
     * 次の曲を再生
     */
    public void nextMusic() {
        Log.i(TAG, "nextMusic");

        // シャッフルがONの場合次の音楽をランダムで指定
        if (m_shuffleflag) {
            shuffleMusic();
            return;
        }

        if(!m_cursor.moveToNext()) {
            m_cursor.moveToFirst();
        }
        m_id = Integer.parseInt(m_cursor.getString(0));
        m_title = m_cursor.getString(1);
        m_path = m_cursor.getString(2);

        Log.i(TAG, m_cursor.getString(0));
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
     * 音楽のループのON・OFFを制御
     * @return m_roopflag  ループのON・OFF
     */
    public boolean setRoopMusic() {
        Log.i(TAG, "setRoopMusic");
        m_roopflag = !m_roopflag;
        return m_roopflag;
    }

    /**
     * 音楽のシャッフルのON・OFFを制御
     * @return m_shuffleflag  シャッフルのON・OFF
     */
    public boolean setShuffleMusic() {
        Log.i(TAG, "setShuffleMusic");
        m_shuffleflag = !m_shuffleflag;
        return m_shuffleflag;
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
     * シャッフルした曲を再生
     */
    private void shuffleMusic() {
        Log.i(TAG, "shuffleMusic");
        m_cursor.moveToPosition(m_rand.nextInt(m_musiclen));

        m_id = Integer.parseInt(m_cursor.getString(0));
        m_title = m_cursor.getString(1);
        m_path = m_cursor.getString(2);

        Log.i(TAG, m_cursor.getString(0));
        Log.i(TAG, m_title);
        Log.i(TAG, m_path);

        createMusic();
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
