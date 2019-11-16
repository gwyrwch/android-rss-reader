package com.example.rssreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.rssreader.ViewModels.ItemViewModel;
import com.example.rssreader.Models.RSSItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
    private ItemViewModel viewModel;
    private static final String DEBUG_TAG = "NetworkStatus";
    private XMLItemListAdapter adapter;

    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new XMLItemListAdapter(this,new View.OnClickListener() {
            // when the note in the MainActivity is tapped
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(v);
                RSSItem itemClicked = ((XMLItemListAdapter) v.getTag()).getItems().get(itemPosition);

                Intent intent = new Intent(MainActivity.this, WebItemActivity.class);

                intent.putExtra(WebItemActivity.ITEM_LINK, itemClicked.link);

                startActivity(intent);
            }
        });



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);


        Log.d(DEBUG_TAG, String.valueOf(isOnline()));


//        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://lenta.ru/rss/news");
        if (isOnline())
            mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://medium.com/feed/the-story");
//        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://news.tut.by/rss/press.rss");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnline()) {
            startDownload();
        }
        else {
            viewModel.getAllItemsByDate(false).observe(this, new Observer<List<RSSItem>>() {
                @Override
                public void onChanged(@Nullable final List<RSSItem> rssItems) {
                    if (rssItems != null)
                        adapter.setItems(rssItems);
                }
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fetch_action:
                // fixme: make settings StartActivityForResult and return url string
                return true;
            // Clear the text and cancel download.
            case R.id.clear_action:
                finishDownloading();
                System.out.println("clear to empty text");
                return true;
        }
        return false;
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
        Log.d("RESULT", "some errors occurred with result");
    }

    @Override
    public void updateFromDownload(ArrayList<RSSItem> result) {
        if (result != null) {
            viewModel.deleteAllItems();
            viewModel.setAllItemsByDate(result);
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
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
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
}
