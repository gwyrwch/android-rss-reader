package com.example.rssreader.XMLParser;

public class XMLItem {
    // title , link , image , description, pubDate

    public final String title;
    public final String link;
    public final String image;
    public final String description;
    public final String pubDate;

    public XMLItem(String title, String link, String image, String description, String pubDate) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.description = description;
        this.pubDate = pubDate;
    }
}
