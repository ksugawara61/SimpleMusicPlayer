package org.example.simplemusicplayer.Model;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by katsuya on 16/07/22.
 */
public class MusicFileFilter implements FilenameFilter {

    // ファイルリストから取得する音楽ファイルの拡張子
    private final String MUSIC_EXTENSION_MP3 = "mp3";
    private final String MUSIC_EXTENSION_M4A = "m4a";

    /**
     * 音楽ファイルか判定
     * @param dir ファイルが見つかったディレクトリ
     * @param filename ファイル名
     * @return 音楽ファイル true, それ以外 false
     */
    public boolean accept(File dir, String filename) {
        // ファイルの拡張子から音楽ファイルか判定
        if (filename.endsWith(MUSIC_EXTENSION_MP3)) {
            return true;
        }
        else if (filename.endsWith(MUSIC_EXTENSION_M4A)) {
            return true;
        }

        return false;
    }
}
