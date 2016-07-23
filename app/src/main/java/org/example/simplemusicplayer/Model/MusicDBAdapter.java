package org.example.simplemusicplayer.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by katsuya on 16/07/23.
 * 音楽ファイルのDBを制御するクラス
 * @version 1.0
 * @author katsuya
 */
public class MusicDBAdapter {

    private static String TAG = "MusicDBAdapter";
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     */
    public MusicDBAdapter(Context context, String name,
                          CursorFactory factory, int version)
    {
        // データベースの生成
        DBHelper db_helper = new DBHelper(context, name, factory, version);
        db = db_helper.getWritableDatabase();
    }

    /**
     * 音楽ファイルの情報をデータベースに格納
     */
    public void insertMusic() {
        File test = new File("/storage/sdcard1/music");
        List<File> music = searchMusicFiles(test);
    }

    /**
     * 音楽ファイルを探索
     * @param root 探索するフォルダパスのルート
     * @return 音楽ファイル一覧
     */
    private List<File> searchMusicFiles(File root) {
        // ルートディレクトリ配下の音楽ファイル
        List<File> music = new ArrayList<File>();
        music.addAll(Arrays.asList(root.listFiles(new MusicFileFilter())));

        // ルートディレクトリ配下のファイル一覧
        List<File> files = Arrays.asList(root.listFiles());

        // ルートディレクトリを再帰的に探索し音楽ファイル一覧を取得
        for (File file : files) {
            Log.d(TAG, "test " + file.getAbsolutePath());

            // ファイルがディレクトリの場合再帰的に探索
            if (file.isDirectory()) {
                music.addAll(searchMusicFiles(file));
            }
        }

        return music;
    }

    /**
     * 音楽ファイルをDBに格納
     */
}
