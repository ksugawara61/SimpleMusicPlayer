package org.example.simplemusicplayer.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by katsuya on 16/07/22.
 * データベースヘルパークラス
 * @version 1.0
 * @author katsuya
 */
public class DBHelper extends SQLiteOpenHelper {

    // テーブル名
    private static final String TABLE_MUSIC = "music";
    private static final String TABLE_ARTIST = "artist";
    private static final String TABLE_GENRE = "genre";
    private static final String TABLE_ALBUM = "album";
    private static final String TABLE_PLAYLIST = "playlist";
    private static final String TABLE_PLAYLIST_MAP = "playlist_map";

    // テーブル生成用SQL
    private static final String CREATE_TABLE_MUSIC = "create table "
            + TABLE_MUSIC + " if not exists ("
            + "music_id" + " integer primary key autoincrement not null, "
            + "music_name" + " text not null, "
            + "music_path" + " text not null"
            + ")";

    // テーブル削除用SQL
    private static final String DROP_TABLE_MUSIC = "drop table " + TABLE_MUSIC;

    /**
     * コンストラクタ
     * @param context コンテキスト
     * @param name    データベース名
     * @param factory
     * @param version データベースバージョン
     */
    public DBHelper(Context context, String name,
                    CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    /**
     * データベースの生成
     * @param db  データベース
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MUSIC);
    }

    /**
     * データベースの更新
     * @param db データベース
     * @param old_version 過去バージョン番号
     * @param new_version 新規バージョン番号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        db.execSQL(DROP_TABLE_MUSIC);
        onCreate(db);
    }
}
