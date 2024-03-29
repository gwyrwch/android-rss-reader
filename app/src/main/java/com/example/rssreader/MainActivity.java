package com.example.rssreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.ViewModels.ItemViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
    public static final int EDIT_RSS_URL_REQUEST_CODE = 1;
    private static String RSS_URL;
//    https://lenta.ru/rss/news
//    https://news.tut.by/rss/press.rss
//    https://meduza.io/rss/all
    private static final String DEBUG_TAG = "NetworkStatus";
    private boolean PATH_CHANGED = false;
    ViewDialog viewDialog;



    private SharedPreferences prefs = null;

    private ItemViewModel viewModel;
    private XMLItemListAdapter adapter;

    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;
    RecyclerView recyclerView;
    private Timer timer;

    public enum State {
        LAUNCH,
        ONLINE,
        OFFLINE
    };
    private State onlineState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineState = State.LAUNCH;

        viewDialog = new ViewDialog(this);

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new XMLItemListAdapter(this,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                RSSItem itemClicked = ((XMLItemListAdapter) v.getTag()).getItems().get(itemPosition);

                Intent intent = new Intent(MainActivity.this, WebItemActivity.class);

                if (onlineState == State.ONLINE) {
                    intent.putExtra(WebItemActivity.ITEM_LINK,  itemClicked.getLink());
                } else {
                    intent.putExtra(WebItemActivity.ITEM_HTML, itemClicked.getHtml());
                }

                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
        String defaultValue = getResources().getString(R.string.default_rss_url);
        RSS_URL = sharedPreferences.getString("rss_url", defaultValue);

        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), RSS_URL);

        Log.d(DEBUG_TAG, String.valueOf(onlineState));

        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).apply();

            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
            String defaultValue = getResources().getString(R.string.default_rss_url);
            RSS_URL = sharedPreferences.getString("rss_url", defaultValue);

            Intent intent = new Intent(this, SettingsActivity.class);

            intent.putExtra(SettingsActivity.URL, RSS_URL);
            startActivityForResult(intent, EDIT_RSS_URL_REQUEST_CODE);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            State state = isOnline();

            if (state != onlineState || PATH_CHANGED) {
                onlineState = state;
                PATH_CHANGED = false;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    if (onlineState == State.ONLINE) {
                        showCustomLoadingDialog();
                    } else {
                        viewModel.getAllItemsByDate(false).observe(MainActivity.this, new Observer<List<RSSItem>>() {
                            @Override
                            public void onChanged(@Nullable final List<RSSItem> rssItems) {
                            if (rssItems != null)
                                adapter.setItems(rssItems);
                            }
                        });
                    }

                    Toast.makeText(
                        getApplicationContext(),
                        onlineState == State.ONLINE ? "Online" : "Offline",
                        Toast.LENGTH_SHORT).show();
                    }
                });
            }
            }
        }, 0, 1000);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings_action) {
            Intent intent = new Intent(this, SettingsActivity.class);

            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
            String defaultValue = getResources().getString(R.string.default_rss_url);
            RSS_URL = sharedPreferences.getString("rss_url", defaultValue);

            intent.putExtra(SettingsActivity.URL, RSS_URL);

            startActivityForResult(intent, EDIT_RSS_URL_REQUEST_CODE);

            return true;
        }
        return false;
    }

    public State isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return State.ONLINE;
        } else {
            return State.OFFLINE;
        }
    }

    // fixme: this two methods are familiar
    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(String result) {
        Log.d("BAD RESULT", result);
    }

    @Override
    public void updateFromDownload(ArrayList<RSSItem> result) {
        if (result != null) {
            viewModel.deleteAllItems();
            viewModel.setAllItemsByDate(result);
            System.out.println("size: " + result.size());

            viewModel.getAllItemsByDate(true).observe(this, new Observer<List<RSSItem>>() {
                @Override
                public void onChanged(@Nullable final List<RSSItem> rssItems) {
                    if (rssItems != null)
                        adapter.setItems(rssItems);
                }
            });

            for (int i = 0; i < Math.min(10, result.size()); i++)
                viewModel.insert(result.get(i));
        } else {
            Log.d("RESULT", "rss list is null");
        }
    }


    @Override
    public void finishDownloading() {

        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    @Override
    public void updateSingleImage(RSSItem item) {
        if (item != null) {
            int position = adapter.getItems().indexOf(item);
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void updateSingleHtml(RSSItem item) {
        System.out.println(item.getTitle() + " loaded");
//        if (item != null) {
//            int position = adapter.getItems().indexOf(item);
//            adapter.notifyItemChanged(position);
//        }
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            case Progress.ERROR:
                Log.d("error", "error in onProgressUpdate");
                break;
            case Progress.CONNECT_SUCCESS:
                Log.d("connect success", "connect success in onProgressUpdate");
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                Log.d("get input stream success", "get input stream success in onProgressUpdate");
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                Log.d("percentComplete", "" + percentComplete + "%");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                Log.d("process input stream success", "process input stream success in onProgressUpdate");
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_RSS_URL_REQUEST_CODE && resultCode == RESULT_OK) {
            RSS_URL = data.getStringExtra(SettingsActivity.URL);
            mNetworkFragment.setUrlString(RSS_URL);
            PATH_CHANGED = true;


            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("rss_url", RSS_URL);
            editor.apply(); //?

        } else {
            System.out.println(requestCode);
            Toast.makeText(
                    getApplicationContext(),
                    "something went wrong",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void showCustomLoadingDialog() {

        viewDialog.showDialog();
        // fixme:
        startDownload();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewDialog.hideDialog();
            }
        }, 3000);
    }

}
