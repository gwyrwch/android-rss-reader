package com.example.rssreader.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


@Entity(tableName = "RSSItem")
public class RSSItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rssItem_id")
    public long id;


    public final String title;
    public final String link;
    public final String image;
    public final String description;
    public final OffsetDateTime pubDate;
    public OffsetDateTime publicationDate;


    public RSSItem(String title, String link, String image, String description, OffsetDateTime pubDate) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.description = description;
        this.pubDate = pubDate;

    }
}
