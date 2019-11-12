package com.example.rssreader.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Repositories.RSSItemRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private RSSItemRepository repository;
    private LiveData<List<RSSItem>> allItemsByDate;


    public ItemViewModel(Application application) {
        super(application);
        repository = new RSSItemRepository(application);
        allItemsByDate = repository.getAllItemsByDate();
    }

    public LiveData<List<RSSItem>> getAllItemsByDate() {
        return allItemsByDate;
    }

    public void insert(RSSItem item) {
        repository.insert(item);
    }

    public void delete(RSSItem item) {
        repository.delete(item);
    }
}
