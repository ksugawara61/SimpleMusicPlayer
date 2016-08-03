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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.example.simplemusicplayer.Model.MusicDBAdapter;
import org.example.simplemusicplayer.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        m_search_results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * リストビューのクリックイベント
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 選択したアイテムの取得
                ListView listView = (ListView)parent;
                HashMap<String, String> item = (HashMap<String, String>)listView.getItemAtPosition(position);
                Log.d(TAG, item.get("title"));

                // 音楽のタイトルを渡して、MainFragmentへ遷移する
            }
        });

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

            // 検索文字が入力される度にリストビューを初期化
            m_search_results.setAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1));

            // 検索結果をリストビューに表示
            if (search_word != null && search_word.length() != 0) {
                m_search_results.setAdapter(getSearchResults(search_word));
            }
            return true;
        }

        /**
         * 検索文字に対応する検索結果を取得
         * @param search_word 検索文字
         * @return リストビューに表示する検索結果
         */
        private SimpleAdapter getSearchResults(String search_word) {
            // DBから検索文字にマッチするレコードを取得
            Cursor cursor = m_adapter.rawQueryMusic(SEARCH_MUSIC_TITLE,
                    new String[]{search_word});

            // リストビューに表示するレコード情報をadapterに追加
            int cnt = cursor.getCount();
            List<Map<String, String>> results_list = new ArrayList<Map<String, String>>();
            for (int i = 0; i < cnt; i++) {
                cursor.moveToPosition(i);
                Map<String, String> tmp = new HashMap<String, String>();
                tmp.put("title", cursor.getString(0));
                tmp.put("other", cursor.getString(1) + " - " + cursor.getString(2));
                results_list.add(tmp);
            }

            SimpleAdapter adapter = new SimpleAdapter(getActivity(), results_list,
                    android.R.layout.simple_list_item_2,
                    new String[] {"title", "other"},
                    new int[] {android.R.id.text1, android.R.id.text2});

            return adapter;
        }
    };

}
