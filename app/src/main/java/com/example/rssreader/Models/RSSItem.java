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

    public OffsetDateTime publicationDate;
    public String title;
    public String imagePath;
    public String author;

    public RSSItem(String title, String imagePath, String author, OffsetDateTime publicationDate) {
        this.title = title;
        this.imagePath = imagePath;
        this.author = author;
        this.publicationDate = publicationDate;

    }

    // fixme: converter from string to OffsetDateTime
//    protected OffsetDateTime getDateForRSSItem(String date) {
////        String input = "Thu, 03 Mar 2016 15:30:00 +0200";
//        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME ;
//        return OffsetDateTime.parse( date , formatter );
//    }
}
