package org.example.simplemusicplayer.Controller;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.AndroidException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.example.simplemusicplayer.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        // ホームアイコン横のHomeAsUpアイコンを有効に。HomeAsUpアイコンは後述のドロワートグルで上書き。
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        //ナビゲーションドロワーの設定
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //第三引数でHomeAsUpアイコンを指定。
        //第四・第五引数は、String.xmlで適当な文字列を。
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                R.string.open, R.string.close) {

            //閉じた時に呼ばれる
            @Override
            public void onDrawerClosed(View drawerView) {
            }

            //開いた時に呼ばれる
            @Override
            public void onDrawerOpened(View drawerView) {
            }

            //アニメーションの処理。Overrideする場合はスーパークラスの同メソッドを呼ぶ。
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            //状態が変化した時に呼ばれる。
            // 表示/閉じ済み -> 0
            // ドラッグ中 -> 1
            // ドラッグを開放た後のアニメーション中 ->2
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);

        //ここではドロワーにMenuFragmentをセットしてみる
        /*menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentMenu, menuFragment).commit();*/
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // 左上メニューボタン表示
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

//        DrawerLayout drawer = (DrawerLayout)findViewById(android.R.id.drawer_layout);

        return true;
    }

    /**
     * メニューの項目がタップされた時の動作
     * @param item
     * @return
     */
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch(item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "tap menu icon");
                break;

/*            case R.id.action_search:
                Log.d(TAG, "tap search");

                SearchFragment fragment = new SearchFragment();
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
                builder.show();
                break;
            default:
                Log.d(TAG, "default");
                break;
        }
        return true;
    }*/

}
