package com.example.rssreader.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rssreader.Models.RSSItem;

import java.util.List;

@Dao
public interface RSSItemDao {
    @Insert
    long insert (RSSItem item);

    @Delete
    void delete(RSSItem item);

    @Query("SELECT * FROM RSSItem ORDER BY RSSItem.title")
    LiveData<List<RSSItem>> sortByTitle();
}
