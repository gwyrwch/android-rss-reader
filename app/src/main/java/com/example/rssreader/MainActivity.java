package com.example.rssreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.ViewModels.ItemViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ItemViewModel viewModel;
    private static final String DEBUG_TAG = "NetworkStatus";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ItemListAdapter adapter = new ItemListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        viewModel.getAllItemsByDate().observe(this, new Observer<List<RSSItem>>() {
            @Override
            public void onChanged(@Nullable final List<RSSItem> rssItems) {
                adapter.setItems(rssItems);
            }
        });

        Log.d(DEBUG_TAG, String.valueOf(isOnline()));


    }


    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
