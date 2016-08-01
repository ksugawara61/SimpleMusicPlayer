package org.example.simplemusicplayer.Controller;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.example.simplemusicplayer.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private AlertDialog m_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
    }

    /**
     * メニューアイコンをセット
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // タイトルを非表示
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_menu_white_24dp);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // 検索のイベント
        SearchView search = (SearchView) MenuItemCompat.getActionView(
                menu.findItem(R.id.action_search));
        search.setOnQueryTextListener(onQueryTextListener);


        return true;
    }

    /**
     * メニューの項目がタップされた時の動作
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch(item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "tap menu icon");
                break;

            case R.id.action_search:
                Log.d(TAG, "tap search");
                MainFragment fragment = new MainFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(android.R.id.content, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.main_settings:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.main_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.help_label);
                builder.setMessage(R.string.help_text);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.ok_label, null);
                m_dialog = builder.show();
                break;
            default:
                Log.d(TAG, "test");
                break;
        }
        return true;
    }

    private SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {
            // SubmitボタンorEnterKeyを押されたら呼び出されるメソッド
            Log.d(TAG, searchWord);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // 入力される度に呼び出される
            Log.d(TAG, newText);
            return false;
        }
    };
}
