package com.example.rssreader.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.rssreader.Database.RSSDatabase;
import com.example.rssreader.Database.RSSItemDao;
import com.example.rssreader.Models.RSSItem;

import java.util.List;

public class RSSItemRepository {
    private RSSItemDao itemDao;
    private LiveData<List<RSSItem>> allItemsByDate;


    public RSSItemRepository(Application application) {
        RSSDatabase db = RSSDatabase.getDatabase(application);
        itemDao = db.rssItemDao();
        allItemsByDate = itemDao.sortByDate();
    }


    public LiveData<List<RSSItem>> getAllItemsByDate() {
        return allItemsByDate;
    }


    public void insert(RSSItem item) {
        new insertAsyncTask(itemDao).execute(item);
    }


    private static class insertAsyncTask extends AsyncTask<RSSItem, Void, RSSItem> {
        private RSSItemDao asyncItemDao;

        insertAsyncTask(RSSItemDao dao) {
            asyncItemDao = dao;
        }

        @Override
        protected RSSItem doInBackground(final RSSItem... params) {
            params[0].id = asyncItemDao.insert(params[0]);
            return params[0];
        }
    }


    public void delete(RSSItem item) {
        new deleteAsyncTask(itemDao).execute(item);
    }


    private static class deleteAsyncTask extends AsyncTask<RSSItem, Void, Void> {
        private RSSItemDao asyncRSSItemDao;

        deleteAsyncTask(RSSItemDao dao) {
            asyncRSSItemDao = dao;
        }

        @Override
        protected Void doInBackground(final RSSItem... params) {
            asyncRSSItemDao.delete(params[0]);
            return null;
        }
    }


    public void deleteAll() {
        new deleteAllAsyncTask(itemDao).execute();
    }


    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private RSSItemDao asyncRSSItemDao;

        deleteAllAsyncTask(RSSItemDao dao) {
            asyncRSSItemDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncRSSItemDao.deleteAll();
            return null;
        }
    }
}
