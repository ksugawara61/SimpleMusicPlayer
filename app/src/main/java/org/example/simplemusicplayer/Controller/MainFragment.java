package org.example.simplemusicplayer.Controller;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.example.simplemusicplayer.Model.MusicService;
import org.example.simplemusicplayer.R;

import static org.example.simplemusicplayer.Model.MusicConstant.*;

/**
 * Created by katsuya on 16/07/21.
 */
public class MainFragment extends Fragment {

    private final static String TAG = MainFragment.class.getSimpleName();
    private MusicService m_service;
    private boolean m_isbound;
    private BroadcastReceiver m_receiver;
    private IntentFilter m_filter;
    private AlertDialog m_dialog;

    // 初期値として表示する項目
    private String m_init_title = null;
    private String m_init_artist = null;
    private String m_init_album = null;

    // ビューの変化する箇所
    private ImageView m_thumbnail;
    private TextView m_title_text;
    private TextView m_artist_text;
    private TextView m_album_text;
    private ImageButton m_loop_button;    // ループボタン
    private ImageButton m_play_button;    // 再生ボタン
    private ImageButton m_shuffle_button; // シャッフルボタン

    /**
     * フラグメントの生成
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);

        // 他フラグメントからのデータがある場合、取得処理
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d(TAG, "get title: " + bundle.getString("title"));
            m_init_title = bundle.getString("title");
        }

        m_receiver = new BroadcastReceiver() {
            /**
             * ブロードキャストを受信するときに呼び出される
             */
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive");
                Bundle bundle = intent.getExtras();
                receiveEvent(bundle.getString(MUSIC_MESSAGE));
            }
        };

        m_filter = new IntentFilter();
        m_filter.addAction("music_action");
        getActivity().registerReceiver(m_receiver, m_filter);

        doBindService();  // サービスをバインド
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
        View root_view = inflater.inflate(R.layout.fragment_main, container, false);

        // メンバ変数に格納
        m_thumbnail = (ImageView)root_view.findViewById(R.id.thumbnail);
        m_thumbnail.setImageResource(R.drawable.no_image);
        m_title_text = (TextView)root_view.findViewById(R.id.title);
        m_artist_text = (TextView)root_view.findViewById(R.id.artist);
        m_album_text = (TextView)root_view.findViewById(R.id.album);

        // 再生ボタンのイベントを作成
        m_play_button = (ImageButton)root_view.findViewById(R.id.play_button);
        m_play_button.setOnClickListener(btnListener);

        // 前へボタンのイベントを作成
        ImageButton prev_button = (ImageButton)root_view.findViewById(R.id.prev_button);
        prev_button.setOnClickListener(btnListener);

        // 次へボタンのイベントを作成
        ImageButton next_button = (ImageButton)root_view.findViewById(R.id.next_button);
        next_button.setOnClickListener(btnListener);

        // ループボタンのイベントを作成
        m_loop_button = (ImageButton)root_view.findViewById(R.id.loop_button);
        m_loop_button.setOnClickListener(btnListener);

        // シャッフルボタンのイベントを作成
        m_shuffle_button = (ImageButton)root_view.findViewById(R.id.shuffle_button);
        m_shuffle_button.setOnClickListener(btnListener);

        return root_view;
    }

    /**
     * メニューアイコンをセット
     * @param menu
     * @return
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        Log.d(TAG, "onCreateOptionsMenu");
    }

    /**
     * サービスの開始
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /**
     * フラグメントが破棄される時に呼び出す
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(m_receiver);  // レシーバの解除
        doUnbindService();  // サービスのアンバインド
        Log.d(TAG, "onDestroy");
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

                SearchFragment fragment = new SearchFragment();
                FragmentTransaction transaction =
                        getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(android.R.id.content, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.main_settings:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.main_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    }

    /**
     * ボタンのクリックイベント
     */
    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch(view.getId()) {
                // 再生ボタン押下時の処理
                case R.id.play_button:
                    Log.d(TAG, "push play button");
                    if (m_service.playMusic()) {
                        m_play_button.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                    else {
                        m_play_button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                    break;

                // 前へボタン押下時の処理
                case R.id.prev_button:
                    Log.d(TAG, "push prev button");
                    m_service.prevMusic();
                    break;

                // 次へボタン押下時の処理
                case R.id.next_button:
                    Log.d(TAG, "push next button");
                    m_service.nextMusic();
                    break;

                // ループボタン押下時の処理
                case R.id.loop_button:
                    Log.d(TAG, "push loop button");
                    if (m_service.setRoopMusic()) {
                        m_loop_button.setImageResource(R.drawable.ic_repeat_black_24dp);
                        Toast.makeText(getContext(), "ループをONに設定しました", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        m_loop_button.setImageResource(R.drawable.ic_repeat_white_24dp);
                        Toast.makeText(getContext(), "ループをOFFに設定しました", Toast.LENGTH_SHORT).show();
                    }
                    break;

                // シャッフルボタン押下時の処理
                case R.id.shuffle_button:
                    Log.d(TAG, "push shuffle button");
                    if (m_service.setShuffleMusic()) {
                        m_shuffle_button.setImageResource(R.drawable.ic_shuffle_black_24dp);
                        Toast.makeText(getContext(), "シャッフルをONに設定しました", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        m_shuffle_button.setImageResource(R.drawable.ic_shuffle_white_24dp);
                        Toast.makeText(getContext(), "シャッフルをOFFに設定しました", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * サービスのコネクション関連処理
     */
    private ServiceConnection m_connection = new ServiceConnection() {
        /**
         * サービスのバインド時に呼び出す
         * @param class_name
         * @param service
         */
        public void onServiceConnected(ComponentName class_name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            m_service = ((MusicService.MusicBinder)service).getService();

            // タイトルが設定されていたら音楽を流す
            if (m_init_title != null) {
                Log.d(TAG, "onServiceConnected: " + m_init_title );
                m_service.playSpecifiedMusic(m_init_title);
            }
        }

        /**
         * サービスのクラッシュ時に呼び出す
         * @param class_name
         */
        public void onServiceDisconnected(ComponentName class_name) {
            Log.d(TAG, "onServiceDisconnected");
            m_service = null;
        }
    };

    /**
     * サービスをバインド（接続）
     */
    void doBindService() {
        Log.d(TAG, "doBindService");
        getActivity().bindService(new Intent(getActivity(), MusicService.class),
                m_connection, Context.BIND_AUTO_CREATE);
        m_isbound = true;
    }

    /**
     * サービスをアンバインド（切断）
     */
    void doUnbindService() {
        Log.d(TAG, "doUnbindService");
        if (m_isbound) {
            // コネクションの解除
            getActivity().unbindService(m_connection);
            m_isbound = false;
        }
    }

    /**
     * ブロードキャスト受信時のイベント
     * @param message ブロードキャストメッセージ
     */
    private void receiveEvent(String message) {
        Log.d(TAG, "receiveEvent: " + message);

        switch(message) {
            // 音楽情報が更新されたらレイアウトに反映
            case MUSIC_EVENT_SET:
                String title = m_service.getMusicTitle();
                String artist = m_service.getMusicArtist();
                String album = m_service.getMusicAlbum();
                setMusicInfo(title, artist, album);
                showNotification(title, artist, album);
                break;

            // 音楽ファイルが存在しない場合
            case MUSIC_EVENT_NOTFOUND:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.not_found_label);
                builder.setMessage(R.string.not_found_text);
                builder.setPositiveButton(R.string.ok_label, null);
                m_dialog = builder.show();
                break;

            default:
                break;
        }
    }

    /**
     * 音楽ファイルの情報をレイアウトに反映
     * @param title  音楽タイトル
     * @param artist アーティスト名
     * @param album  アルバム名
     */
    private void setMusicInfo(String title, String artist, String album) {
        Log.d(TAG, "setMusicInfo");

        // サムネイル画像を設定
        MediaMetadataRetriever media_data = new MediaMetadataRetriever();
        media_data.setDataSource(m_service.getMusicPath());
        byte[] thumbnail_image =media_data.getEmbeddedPicture();
        if (thumbnail_image != null) {
            m_thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(thumbnail_image,
                    0, thumbnail_image.length));
        }
        // サムネイル画像がない場合 No Image画像を表示
        else {
            m_thumbnail.setImageResource(R.drawable.no_image);
        }

        // 曲のタイトルを設定
        m_title_text.setText(m_service.getMusicTitle());
        m_artist_text.setText(m_service.getMusicArtist());
        m_album_text.setText(m_service.getMusicAlbum());

        // 再生ボタンを変更
        m_play_button.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    /**
     * ノティフィケーションを表示
     * @param title  音楽タイトル
     * @param artist アーティスト名
     * @param album  アルバム名
     */
    private void showNotification(String title, String artist, String album) {
        Log.d(TAG, "showNotification");

        Intent result_intent = new Intent(getActivity(), MainActivity.class);

        PendingIntent result_pending_intent = PendingIntent.getActivity(
                getActivity(), 0, result_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.mipmap.ic_music_note_white_24dp)
                        .setContentTitle(title)
                        .setContentText(artist + " - " + album)
                        .setTicker(title + " - " + artist);

        builder.setContentIntent(result_pending_intent);

        // アクションボタンを追加
        builder.addAction(R.drawable.ic_skip_previous_black_24dp, "前の曲", null);
        builder.addAction(R.drawable.ic_pause_black_24dp, "停止", null);
        builder.addAction(R.drawable.ic_skip_next_black_24dp, "次の曲", null);

        int mNotificationId = 001;

        NotificationManager mNotifyMgr =
                (NotificationManager)getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, builder.build());
    }

}
