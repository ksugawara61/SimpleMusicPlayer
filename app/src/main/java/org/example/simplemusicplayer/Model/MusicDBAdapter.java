package org.example.simplemusicplayer.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
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

    private static final String TAG = "MusicDBAdapter";
    private static final String TABLE_MUSIC = "music";
    private SQLiteDatabase db;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param name    データベース名
     * @param factory
     * @param version データベースバージョン
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
        insertMusicFiles(music);
    }

    /**
     * 生SQLで音楽ファイルデータベースからの値を取得
     * @param sql SQL文
     * @param selection_args プレースホルダーに入力する値
     * @return result SQLの結果
     */
    public Cursor rawQueryMusic(String sql, String[] selection_args) {
        Log.i(TAG, "rawQueryMusic");
        Cursor result = db.rawQuery(sql, selection_args);
        return result;
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
            // ファイルがディレクトリの場合再帰的に探索
            if (file.isDirectory()) {
                music.addAll(searchMusicFiles(file));
            }
        }

        return music;
    }

    /**
     * 音楽ファイルをDBに格納
     * @param music 音楽ファイル一覧
     */
    private void insertMusicFiles(List<File> music) {
        for (File file : music) {

            try {
                // DBに挿入する値を取得
                FileInputStream in = new FileInputStream(file);
                FileChannel channel = in.getChannel();
                channel.position(channel.size() - 128);
                byte[] w = new byte[128];

                in.read(w);

                String title = new String(w, 3, 30, "Shift_JIS").replaceAll("'", "\"");
                String artist = new String(w, 33, 30, "Shift_JIS").replaceAll("'", "\"");
                String album = new String(w, 63, 30, "Shift_JIS").replaceAll("'", "\"");
                Log.i(TAG, "music: " + title + " path: " + file.getAbsolutePath());

                // レコードに格納するパラメータを付与
                ContentValues values = new ContentValues();
                values.put("music_name", title);
                values.put("music_path", file.getAbsolutePath());

                // 既にDBに登録されているレコードか確認し、登録されていない場合新規追加
                String[] where_args = {title};
                int update_num = db.update(TABLE_MUSIC, values, "music_name = ?", where_args);
                if (update_num == 0) {
                    db.insert(TABLE_MUSIC, "", values);
                }
            }
            catch (FileNotFoundException e) {
                Log.e(TAG, e.toString());
            }
            catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
