package com.example.rssreader;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.ViewModels.ItemViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ItemViewModel viewModel;


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

    }
}
