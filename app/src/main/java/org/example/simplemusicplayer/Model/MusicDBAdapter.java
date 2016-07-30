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
            String artist_id = null;
            if (artist != null) {
                artist_id = insertMusicInfo(TABLE_ARTIST, COLUMN_ARTIST_NAME,
                        artist, SELECT_ARTIST_ID);
                Log.d(TAG, "artist_id: " + artist_id);
            }

            // アルバム名をテーブルに格納する処理を実装

            // ジャンル名をテーブルに格納する処理を実装

            // 音楽テーブルに格納するパラメータを付与
            ContentValues values = new ContentValues();
            values.put(COLUMN_MUSIC_TITLE, title);
            values.put(COLUMN_ARTIST_ID, artist_id);
//            values.put("music_album", album);
            values.put(COLUMN_MUSIC_PATH, path);

            // 既にDBに登録されているレコードか確認し、登録されていない場合新規追加
            int update_num = db.update(TABLE_MUSIC, values,
                    COLUMN_MUSIC_TITLE + " = ?", new String[]{title});
            if (update_num == 0) {
                db.insert(TABLE_MUSIC, "", values);
            }
        }
    }

    /**
     * DBに音楽の情報（アーティスト名・アルバム名・ジャンル名を格納しIDを返す
     * @param
     * @return id 各情報のID
     */
    String insertMusicInfo(String table_name, String value_name, String value, String select_sql) {
        Log.d(TAG, "insertMusicInfo");
        String id = null;

        ContentValues values = new ContentValues();
        values.put(value_name, value);
        int update_num = db.update(table_name, values,
                value_name + " = ?", new String[]{value});
        if (update_num == 0) {
            db.insert(table_name, "", values);
        }
        Cursor cursor = db.rawQuery(select_sql, new String[]{value});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getString(0);
        }

        return id;
    }
}
