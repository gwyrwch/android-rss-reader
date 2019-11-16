package com.example.rssreader.ViewModels;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Repositories.RSSItemRepository;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private RSSItemRepository repository;
    private LiveData<List<RSSItem>> localAllItemsByDate, allItemsByDate;


    public void setAllItemsByDate(ArrayList<RSSItem> allItemsByDate) {
        this.allItemsByDate = new LiveData<List<RSSItem>>(allItemsByDate){};
    }

    public ItemViewModel(Application application) {
        super(application);
        repository = new RSSItemRepository(application);
        localAllItemsByDate = repository.getAllItemsByDate();
    }

    public LiveData<List<RSSItem>> getAllItemsByDate(boolean isOnline) {
        if (!isOnline)
            return localAllItemsByDate;
        else
            return allItemsByDate;
    }

    public void insert(RSSItem item) {
        repository.insert(item);
    }

    public void delete(RSSItem item) {
        repository.delete(item);
    }

    public void deleteAllItems()  {repository.deleteAll();}
}
