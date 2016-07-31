package org.example.simplemusicplayer.Model;

/**
 * Created by katsuya on 16/07/31.
 * 音楽サービス関連の定数クラス
 * @version 1.0
 * @author katsuya
 */
public class MusicConstant {

    private MusicConstant() {}  // インスタンス化できないように実装

    public static final String MUSIC_ACTION = "music_action";
    public static final String MUSIC_MESSAGE = "music_message";

    // 音楽サービスのイベント
    public static final String MUSIC_EVENT_SET = "music_set";
    public static final String MUSIC_EVENT_NOTFOUND = "music_notfound";
}
