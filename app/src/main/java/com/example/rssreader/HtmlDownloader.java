package com.example.rssreader;

import android.os.AsyncTask;

import com.example.rssreader.Models.RSSItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HtmlDownloader {
    public static String result;

    public void download(String url, RSSItem item, DownloadCallback callback) {
        result = null;
        HtmlDownloader.HtmlDownloaderTask task = new HtmlDownloader.HtmlDownloaderTask(item, callback);
        task.execute(url);
    }

    static class HtmlDownloaderTask extends AsyncTask<String, Void, String> {
        private final RSSItem item;
        private final DownloadCallback downloadCallback;

        public HtmlDownloaderTask(RSSItem item, DownloadCallback callback) {
            this.item = item;
            this.downloadCallback = callback;
        }

        public String downloadBitmap(String content) {
            URL url;
            String res = "";

            try {
                // get URL content

                url = new URL(content);
                URLConnection conn = url.openConnection();

                // open the stream and put it into BufferedReader
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    res += inputLine;
                }
                br.close();

                System.out.println("Done");
                return res;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected String doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            return downloadBitmap(params[0]);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(String html) {
            if (isCancelled()) {
                html = null;
            }

            // fixme:
            System.out.println(item.getTitle() + " finished id = "  + item.id);
            item.setHtml(html);
//            item.setBitmap(ByteBitmapConverter.getBytesFromBitmap(bitmap));
            downloadCallback.updateSingleHtml(item);
        }
    }
}
