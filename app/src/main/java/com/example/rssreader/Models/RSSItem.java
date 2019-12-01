package com.example.rssreader.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.OffsetDateTime;


@Entity(tableName = "RSSItem")
public class RSSItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rssItem_id")
    public long id;


    private final String title;
    private final String link;
    private final String image;
    private final String description;
    private final OffsetDateTime pubDate;

    public final String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    private String html;
    private OffsetDateTime publicationDate;


    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getPubDate() {
        return pubDate;
    }

    public OffsetDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(OffsetDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] bitmap;


    public RSSItem(String title, String link, String image, String description, OffsetDateTime pubDate,
                   byte[] bitmap, String html
    ) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.description = description;
        this.pubDate = pubDate;
        this.bitmap = bitmap;
        this.html = html;
    }
}
