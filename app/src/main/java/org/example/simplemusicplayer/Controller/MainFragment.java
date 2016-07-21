package org.example.simplemusicplayer.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.example.simplemusicplayer.R;

/**
 * Created by katsuya on 16/07/21.
 */
public class MainFragment extends Fragment {

    private AlertDialog m_dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        View root_view = inflater.inflate(R.layout.fragment_main, container, false);

        // 再生ボタンのイベントを作成
        View play_button = root_view.findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ここに曲の再生 or 停止のイベントを作成

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("play music");
                builder.setMessage("Now, You play the music");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                m_dialog = builder.show();
            }
        });

        // 前へボタンのイベントを作成
        View prev_button = root_view.findViewById(R.id.prev_button);
        prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ここで前の曲を再生

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("prev music");
                builder.setMessage("Now, You play the music");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                m_dialog = builder.show();
            }
        });

        // 次へボタンのイベントを作成
        View next_button = root_view.findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ここで次の曲を再生

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("next music");
                builder.setMessage("Now, You play the music");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                m_dialog = builder.show();
            }
        });

        // ループボタンのイベントを作成
        View roop_button = root_view.findViewById(R.id.roop_button);
        roop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ここでループのON/OFF設定

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("roop music");
                builder.setMessage("Now, You play the music");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                m_dialog = builder.show();
            }
        });

        // シャッフルボタンのイベントを作成
        View shuffle_button = root_view.findViewById(R.id.shuffle_button);
        shuffle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ここでループのON/OFF設定

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("shuffle music");
                builder.setMessage("Now, You play the music");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", null);
                m_dialog = builder.show();
            }
        });

        return root_view;
    }
}
