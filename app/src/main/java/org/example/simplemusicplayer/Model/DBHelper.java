package org.example.simplemusicplayer.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static org.example.simplemusicplayer.Model.DBConstant.*;

/**
 * Created by katsuya on 16/07/22.
 * データベースヘルパークラス
 * @version 1.0
 * @author katsuya
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

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
        Log.d(TAG, "DBHelper");
    }

    /**
     * データベース接続時に呼び出し
     * @param db  データベース
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        Log.d(TAG, "onConfigure");
        // 外部キー制約を有効化
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * データベースの生成
     * @param db  データベース
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
        db.execSQL(CREATE_TABLE_ARTIST);
        db.execSQL(CREATE_TABLE_ALBUM);
        db.execSQL(CREATE_TABLE_GENRE);
        db.execSQL(CREATE_TABLE_MUSIC);
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_PLAYLIST_MAP);
    }

    /**
     * データベースの更新
     * @param db データベース
     * @param old_version 過去バージョン番号
     * @param new_version 新規バージョン番号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        Log.d(TAG, "onUpgrade");
        db.execSQL(DROP_TABLE_PLAYLIST_MAP);
        db.execSQL(DROP_TABLE_PLAYLIST);
        db.execSQL(DROP_TABLE_MUSIC);
        db.execSQL(DROP_TABLE_ARTIST);
        db.execSQL(DROP_TABLE_ALBUM);
        db.execSQL(DROP_TABLE_GENRE);
        onCreate(db);
    }
}
