package com.example.rssreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rssreader.ViewModels.ItemViewModel;
import com.example.rssreader.Models.RSSItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DownloadCallback {
    private ItemViewModel viewModel;
    private static final String DEBUG_TAG = "NetworkStatus";
    private XMLItemListAdapter adapter;

    private NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new XMLItemListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

//        viewModel.getAllItemsByDate().observe(this, new Observer<List<RSSItem>>() {
//            @Override
//            public void onChanged(@Nullable final List<RSSItem> rssItems) {
//                adapter.setItems(rssItems);
//            }
//        });

//        adapter.setItems();

        Log.d(DEBUG_TAG, String.valueOf(isOnline()));


        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://lenta.ru/rss/news");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startDownload();
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
                //
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



    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            System.out.println(result.length());
            System.out.println(result);

        } else {
            Log.d("RESULT", "connection error");
//            mDataText.setText(getString("connection error"));
        }
    }

    @Override
    public void updateFromDownload(ArrayList<RSSItem> result) {
        if (result != null) {
//            System.out.println(result.length());
            System.out.println("pubdate: " + result.get(0).pubDate);
            System.out.println("desc: " + result.get(0).description);
            System.out.println(result.get(0).link);
            adapter.setItems(result);

        } else {
            Log.d("RESULT1", "connection error");
//            mDataText.setText(getString("connection error"));
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
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
