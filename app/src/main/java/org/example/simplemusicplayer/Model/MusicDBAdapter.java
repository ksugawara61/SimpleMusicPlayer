package org.example.simplemusicplayer.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.simplemusicplayer.Model.DBConstant.*;
import static android.media.MediaMetadataRetriever.METADATA_KEY_TITLE;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ALBUM;

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
        Log.d(TAG, "MusicDBAdapter");

        // データベースの生成
        DBHelper db_helper = new DBHelper(context, name, factory, version);
        db = db_helper.getWritableDatabase();
    }

    /**
     * 音楽ファイルの情報をデータベースに格納
     */
    public void insertMusic() {
        Log.d(TAG, "insertMusic");
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
        Log.d(TAG, "rawQueryMusic");
        Cursor result = db.rawQuery(sql, selection_args);
        return result;
    }

    /**
     * 音楽ファイルを探索
     * @param root 探索するフォルダパスのルート
     * @return 音楽ファイル一覧
     */
    private List<File> searchMusicFiles(File root) {
        Log.d(TAG, "searchMusicFiles");
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
        Log.d(TAG, "insertMusicFiles");
        for (File file : music) {
            // 音楽ファイルのメタデータの読み込み
            String path = file.getAbsolutePath();
            MediaMetadataRetriever media_data = new MediaMetadataRetriever();
            media_data.setDataSource(path);

            // 音楽ファイルのメタデータを取得しDBに格納
            String title = media_data.extractMetadata(METADATA_KEY_TITLE);
            String artist = media_data.extractMetadata(METADATA_KEY_ARTIST);
            String album = media_data.extractMetadata(METADATA_KEY_ALBUM);
            Log.d(TAG, "music: " + title + " - " + artist + " - " + album + " - " + path);

            // アーティスト名をテーブルに格納する処理を実装

            // アルバム名をテーブルに格納する処理を実装

            // ジャンル名をテーブルに格納する処理を実装

            // 音楽テーブルに格納するパラメータを付与
            ContentValues values = new ContentValues();
            values.put(COLUMN_MUSIC_TITLE, title);
//            values.put("music_artist", artist);
//            values.put("music_album", album);
            values.put(COLUMN_MUSIC_PATH, path);

            // 既にDBに登録されているレコードか確認し、登録されていない場合新規追加
            String[] where_args = {title};
            int update_num = db.update(TABLE_MUSIC, values,
                    COLUMN_MUSIC_TITLE + " = ?", where_args);
            if (update_num == 0) {
                db.insert(TABLE_MUSIC, "", values);
            }
        }
    }
}
