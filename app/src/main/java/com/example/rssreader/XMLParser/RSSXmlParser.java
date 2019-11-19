package com.example.rssreader.XMLParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Xml;

import com.example.rssreader.ImageDownloader;
import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Utilities.ByteBitmapConverter;
import com.example.rssreader.Utilities.OffsetDateTimeToStringConverter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
                    pubDate = readPubDate(parser);
                    break;
                case "enclosure":
                    image = readImage(parser);
                    break;
                case "content:encoded":
                    String content = getTextFromTag(parser);
                    image = readImageFromContent(content);
                    description = readDescriptionFromContent(content);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

//        GetBitmapFromURLAsync getBitmapFromURLAsync = new GetBitmapFromURLAsync();
//        getBitmapFromURLAsync.execute(image);
        RSSItem newItem = new RSSItem(title, link, image, description,
                OffsetDateTimeToStringConverter.getDateFromString(pubDate),
                null);
        ImageDownloader id = new ImageDownloader();
        id.download(image, newItem);

        return newItem;
//                ByteBitmapConverter.getBytesFromBitmap(downloadedBitmap)
    }

    private String readImageFromContent(String content) {
        int start = content.indexOf("src=\"");
        int end = content.indexOf("\"", start + 5);
        return content.substring(start + 5, end);
    }

    private String readDescriptionFromContent(String content) {
        int start = content.indexOf("<h4>");
        int end = content.indexOf("</h4>");
        return  content.substring(start + 4, end);
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

    private Bitmap downloadedBitmap;
    private class GetBitmapFromURLAsync extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //  return the bitmap by doInBackground and store in result
            downloadedBitmap = bitmap;
        }

        private Bitmap downloadBitmap(String src) {
            try {
                java.net.URL url = new java.net.URL(src);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
