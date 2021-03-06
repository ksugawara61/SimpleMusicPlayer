package org.example.simplemusicplayer.Controller;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.simplemusicplayer.Model.DBConstant.DB_NAME;
import static org.example.simplemusicplayer.Model.DBConstant.SEARCH_MUSIC_TITLE;

/**
 * Created by katsuya on 16/08/03.
 * 音楽ファイルの検索結果を表示するフラグメントクラス
 */
public class SearchFragment extends Fragment {

    private final static String TAG = SearchFragment.class.getSimpleName();
    private SearchFragment m_search_fragment;
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
        m_search_fragment = this;
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

        // キーのタップイベント
        root_view.setFocusableInTouchMode(true);
        root_view.setOnKeyListener(onKeyListener);

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
                Log.d(TAG, "onItemClick title: " + item.get("title"));

                // リストビューのクリックを検知したらキーボードを閉じる
                InputMethodManager input = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                // 音楽情報をMainFragmentに設定
                MainFragment fragment = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title", item.get("title"));

                Matcher matcher = Pattern.compile("(.*) - (.*)").matcher(item.get("other"));
                if (matcher.find()) {
                    Log.d(TAG, "onItemClick artist: " + matcher.group(1));
                    Log.d(TAG, "onItemClick album: " + matcher.group(2));
                    bundle.putString("artist", matcher.group(1));
                    bundle.putString("album", matcher.group(2));
                }

                fragment.setArguments(bundle);

                // MainFragmentへ遷移する
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, fragment);
                transaction.remove(m_search_fragment);
                transaction.commit();
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
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search),
                onActionExpandListener);
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
        m_search_results = null;
    }

    /**
     * 検索バーのイベント
     */
    private MenuItemCompat.OnActionExpandListener onActionExpandListener =
            new MenuItemCompat.OnActionExpandListener()
    {
        /**
         * 検索バーを閉じるボタンを押した時の動作
         * @param item
         * @return
         */
        @Override
        public boolean onMenuItemActionCollapse(MenuItem item) {
            Log.d(TAG, "search view collapse");
            getFragmentManager().popBackStack();  // MainFragmentへ戻る
            return true;
        }

        /**
         *
         * @param item
         * @return
         */
        @Override
        public boolean onMenuItemActionExpand(MenuItem item) {
            Log.d(TAG, "search view expand");
            return true;
        }
    };

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

            if (m_search_results == null) {
                Log.d(TAG, "m_search_results is null");
                return false;
            }

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

    /**
     * キーの入力イベント
     */
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        /**
         * キーのタップイベント
         * @param view
         * @param key_code
         * @param event
         * @return
         */
        @Override
        public boolean onKey(View view, int key_code, KeyEvent event) {
            switch (key_code) {
                // バックキーがタップされた時のイベント
                case (KeyEvent.KEYCODE_BACK):
                    Log.d(TAG, "tap back key");
                    getFragmentManager().popBackStack();
                    return true;

                default:
                    break;
            }

            return false;
        }
    };
}
