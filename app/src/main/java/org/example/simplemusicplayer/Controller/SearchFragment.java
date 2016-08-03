package org.example.simplemusicplayer.Controller;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import org.example.simplemusicplayer.Model.MusicDBAdapter;
import org.example.simplemusicplayer.R;

import static org.example.simplemusicplayer.Model.DBConstant.DB_NAME;
import static org.example.simplemusicplayer.Model.DBConstant.SEARCH_MUSIC_TITLE;
import static org.example.simplemusicplayer.Model.DBConstant.SELECT_ALL_MUSIC;

/**
 * Created by katsuya on 16/08/03.
 * 音楽ファイルの検索結果を表示するフラグメントクラス
 */
public class SearchFragment extends Fragment {

    private final static String TAG = SearchFragment.class.getSimpleName();
    private ListView m_search_results;
    private SearchView m_search;
    private MusicDBAdapter m_adapter = null;  // 音楽DB

    /**
     * フラグメントの生成
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
        m_adapter = new MusicDBAdapter(getContext(), DB_NAME, null, 1);
    }

    /**
     * ビューの生成
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        View root_view = inflater.inflate(R.layout.fragment_search, container, false);
        getActivity().getMenuInflater();

        // メンバ変数に格納
        m_search_results = (ListView)root_view.findViewById(R.id.search_results);

        return root_view;
    }

    /**
     * メニューアイコンのイベントを設定
     * @param menu
     * @param menuInflater
     * @return
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        Log.d(TAG, "onCreateOptionsMenu");

        // 検索バーのイベントを設定
        SearchView m_search = (SearchView)MenuItemCompat.getActionView(
                menu.findItem(R.id.action_search));
        m_search.setOnQueryTextListener(onQueryTextListener);
    }

    /**
     * フラグメントが破棄される時に呼び出す
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * 検索バーのイベント
     */
    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {

        /**
         * SubmitボタンorEnterKeyを押されたら呼び出されるメソッド
         * @param search_word
         * @return
         */
        @Override
        public boolean onQueryTextSubmit(String search_word) {
            Log.d(TAG, "onQueryTextSubmit: " + search_word);
            return true;
        }

        /**
         * 検索バーに検索文字が入力される度に呼び出す
         * @param search_word 入力された文字列
         * @return
         */
        @Override
        public boolean onQueryTextChange(String search_word) {
            Log.d(TAG, "onQueryTextChange: " + search_word);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1);
            // 検索文字が入力される度にリストビューを初期化
            m_search_results.setAdapter(adapter);

            // 検索結果をリストビューに表示
            if (search_word != null && search_word.length() != 0) {
                // DBから検索文字にマッチするレコードを取得
                Cursor cursor = m_adapter.rawQueryMusic(SEARCH_MUSIC_TITLE,
                        new String[]{search_word});
                // リストビューに表示するレコード情報をadapterに追加
                int cnt = cursor.getCount();
                for (int i = 0; i < cnt; i++) {
                    cursor.moveToPosition(i);
                    adapter.add(cursor.getString(0));
                }
                m_search_results.setAdapter(adapter);
            }
            return true;
        }
    };

}
