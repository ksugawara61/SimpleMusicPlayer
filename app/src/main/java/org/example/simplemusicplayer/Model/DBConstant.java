package org.example.simplemusicplayer.Model;

/**
 * Created by katsuya on 16/07/26.
 * データベース関連の定数クラス
 * @version 1.0
 * @author katsuya
 */
public final class DBConstant {

    private DBConstant() {}  // インスタンス化できないように実装

    // DB名
    public static final String DB_NAME = "simplemusicplayer.db";

    // テーブル名
    public static final String TABLE_MUSIC = "music";
    public static final String TABLE_ARTIST = "artist";
    public static final String TABLE_ALBUM = "album";
    public static final String TABLE_GENRE = "genre";
    public static final String TABLE_PLAYLIST = "playlist";
    public static final String TABLE_PLAYLIST_MAP = "playlist_map";

    // musicテーブルのカラム名
    public static final String COLUMN_MUSIC_ID = "music_id";
    public static final String COLUMN_MUSIC_TITLE = "music_title";
    public static final String COLUMN_MUSIC_PATH = "music_path";

    // artistテーブルのカラム名
    public static final String COLUMN_ARTIST_ID = "artist_id";
    public static final String COLUMN_ARTIST_NAME = "artist_name";

    // albumテーブルのカラム名
    public static final String COLUMN_ALBUM_ID = "album_id";
    public static final String COLUMN_ALBUM_NAME = "album_name";

    // genreテーブルのカラム名
    public static final String COLUMN_GENRE_ID = "genre_id";
    public static final String COLUMN_GENRE_NAME = "genre_name";

    // playlistテーブルのカラム名
    public static final String COLUMN_PLAYLIST_ID = "playlist_id";
    public static final String COLUMN_PLAYLIST_NAME ="playlist_name";

    // playlistテーブルのカラム名
    public static final String COLUMN_MAP_ID = "map_id";

    // musicテーブル生成用SQL
    public static final String CREATE_TABLE_MUSIC = "create table if not exists "
            + TABLE_MUSIC + " ("
            + COLUMN_MUSIC_ID + " integer primary key autoincrement not null, "
            + COLUMN_MUSIC_TITLE + " text not null, "
            + COLUMN_ARTIST_ID + " integer, "
            + COLUMN_ALBUM_ID + " integer, "
            + COLUMN_GENRE_ID + " integer, "
            + COLUMN_MUSIC_PATH + " text not null, "
            + "foreign key("+ COLUMN_ARTIST_ID + ") references "
            + TABLE_ARTIST + "(" + COLUMN_ARTIST_ID + "), "
            + "foreign key("+ COLUMN_ALBUM_ID + ") references "
            + TABLE_ALBUM + "(" + COLUMN_ALBUM_ID + "), "
            + "foreign key("+ COLUMN_GENRE_ID + ") references "
            + TABLE_GENRE + "(" + COLUMN_GENRE_ID + ")"
            + ")";

    // artistテーブル生成用SQL
    public static final String CREATE_TABLE_ARTIST = "create table if not exists "
            + TABLE_ARTIST + " ("
            + COLUMN_ARTIST_ID + " integer primary key autoincrement not null, "
            + COLUMN_ARTIST_NAME + " text not null"
            + ")";

    // artistテーブル生成用SQL
    public static final String CREATE_TABLE_ALBUM = "create table if not exists "
            + TABLE_ALBUM + " ("
            + COLUMN_ALBUM_ID + " integer primary key autoincrement not null, "
            + COLUMN_ALBUM_NAME + " text not null"
            + ")";

    // artistテーブル生成用SQL
    public static final String CREATE_TABLE_GENRE = "create table if not exists "
            + TABLE_GENRE + " ("
            + COLUMN_GENRE_ID + " integer primary key autoincrement not null, "
            + COLUMN_GENRE_NAME + " text not null"
            + ")";

    // playlistテーブル生成用SQL
    public static final String CREATE_TABLE_PLAYLIST = "create table if not exists "
            + TABLE_PLAYLIST + " ("
            + COLUMN_PLAYLIST_ID + " integer primary key autoincrement not null, "
            + COLUMN_PLAYLIST_NAME + " text not null"
            + ")";

    // playlist_mapテーブル生成用SQL
    public static final String CREATE_TABLE_PLAYLIST_MAP = "create table if not exists "
            + TABLE_PLAYLIST_MAP + " ("
            + COLUMN_MAP_ID + " integer primary key autoincrement not null, "
            + COLUMN_PLAYLIST_ID + " integer not null, "
            + COLUMN_MUSIC_ID + " integer not null, "
            + "foreign key("+ COLUMN_PLAYLIST_ID + ") references "
            + TABLE_PLAYLIST + "(" + COLUMN_PLAYLIST_ID + "), "
            + "foreign key("+ COLUMN_MUSIC_ID + ") references "
            + TABLE_MUSIC + "(" + COLUMN_MUSIC_ID + ")"
            + ")";

    // テーブル削除用SQL
    public static final String DROP_TABLE_MUSIC = "drop table " + TABLE_MUSIC;
    public static final String DROP_TABLE_ARTIST = "drop table " + TABLE_ARTIST;
    public static final String DROP_TABLE_ALBUM = "drop table " + TABLE_ALBUM;
    public static final String DROP_TABLE_GENRE = "drop table " + TABLE_GENRE;
    public static final String DROP_TABLE_PLAYLIST = "drop table " + TABLE_PLAYLIST;
    public static final String DROP_TABLE_PLAYLIST_MAP = "drop table " + TABLE_PLAYLIST_MAP;

    // ARTIST_ID取得用SQL
    public static final String SELECT_ARTIST_ID = "select " + COLUMN_ARTIST_ID + " from "
            + TABLE_ARTIST + " where " + COLUMN_ARTIST_NAME + " = ? limit 1";

    // ALBUM_ID取得用SQL
    public static final String SELECT_ALBUM_ID = "select " + COLUMN_ALBUM_ID + " from "
            + TABLE_ALBUM + " where " + COLUMN_ALBUM_NAME + " = ? limit 1";

    // 全音楽リスト取得用SQL
    public static final String SELECT_ALL_MUSIC = "select " + COLUMN_MUSIC_ID + ", "
            + COLUMN_MUSIC_TITLE + ", " + COLUMN_ARTIST_NAME + ", "// + COLUMN_MUSIC_PATH + " from "
            + COLUMN_ALBUM_NAME + ", " + COLUMN_MUSIC_PATH + " from " + TABLE_MUSIC
            + " inner join " + TABLE_ARTIST + " on " + TABLE_MUSIC + "." + COLUMN_ARTIST_ID
            + " = " + TABLE_ARTIST + "." + COLUMN_ARTIST_ID + " "
            + " inner join " + TABLE_ALBUM + " on " + TABLE_MUSIC + "." + COLUMN_ALBUM_ID
            + " = " + TABLE_ALBUM + "." + COLUMN_ALBUM_ID;
}
