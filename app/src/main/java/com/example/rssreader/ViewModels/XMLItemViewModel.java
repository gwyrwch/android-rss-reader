package com.example.rssreader.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rssreader.Models.RSSItem;

import java.util.List;

public class XMLItemViewModel extends AndroidViewModel {
    private LiveData<List<RSSItem>> allItemsByDate;

    public XMLItemViewModel(@NonNull Application application) {
        super(application);
    }
}
