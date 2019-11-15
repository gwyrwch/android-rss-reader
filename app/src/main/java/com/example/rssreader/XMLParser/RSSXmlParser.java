package com.example.rssreader.XMLParser;

import android.util.Xml;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Utilities.OffsetDateTimeConverter;
import com.example.rssreader.Utilities.OffsetDateTimeToStringConverter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RSSXmlParser {
    private static final String namespaces = null;

    public List<RSSItem> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            return readRSS(parser);

        } finally {
            in.close();
        }
    }

    private ArrayList<RSSItem> readRSS(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        ArrayList<RSSItem> items = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                items.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    private List<RSSItem> readChannel(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<RSSItem> items = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, namespaces, "channel");


        do {
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")) {
                    items.add(readItem(parser));
                } else {
                    skip(parser);
                }

            }
        } while (!parser.getName().equals("channel"));

        return items;
    }

    private RSSItem readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespaces, "item");
        String title = null;
        String link = null;
        String image = null;
        String description = null;
        String pubDate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    title = readTitle(parser);
                    break;
                case "link":
                    link = readLink(parser);
                    break;
                case "description":
                    description = readDescription(parser);
                    break;
                case "pubDate":
                    System.out.println("in pb date:");
                    pubDate = readPubDate(parser);
                    System.out.println(pubDate);
                    break;
                case "enclosure":
                    image = readImage(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new RSSItem(title, link, image, description, OffsetDateTimeToStringConverter.getDateFromString(pubDate));
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespaces, "title");
        String title = getTextFromTag(parser);
        parser.require(XmlPullParser.END_TAG, namespaces, "title");
        return title;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespaces, "pubDate");
        String pubDate = getTextFromTag(parser);
        parser.require(XmlPullParser.END_TAG, namespaces, "pubDate");
        return pubDate;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespaces, "description");
        String description = getTextFromTag(parser);
        parser.require(XmlPullParser.END_TAG, namespaces, "description");
        return description;
    }

    private String readImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        String img = "";
        parser.require(XmlPullParser.START_TAG, namespaces, "enclosure");
//        String tag = parser.getName();

        img = parser.getAttributeValue(null, "url");
        parser.nextTag();

        parser.require(XmlPullParser.END_TAG, namespaces, "enclosure");
        return img;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, namespaces, "link");
        String link = getTextFromTag(parser);
        parser.require(XmlPullParser.END_TAG, namespaces, "link");
        return link;
    }

     private String getTextFromTag(XmlPullParser parser) throws IOException, XmlPullParserException {
         String result = "";
         if (parser.next() == XmlPullParser.TEXT) {
             result = parser.getText();
             parser.nextTag();
         } else {
             result = parser.getText();
             parser.nextTag();
         }
         return result;
     }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}