package com.example.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.rssreader.Models.RSSItem;
import com.example.rssreader.Utilities.ByteBitmapConverter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class BitmapDownloader {
    public static Bitmap result;

    public void download(String url, RSSItem item, DownloadCallback callback) {
        result = null;
        BitmapDownloaderTask task = new BitmapDownloaderTask(item, callback);
        task.execute(url);
    }

    static class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final RSSItem item;
        private final DownloadCallback downloadCallback;

        public BitmapDownloaderTask(RSSItem item, DownloadCallback callback) {
            this.item = item;
            this.downloadCallback = callback;
        }

        public Bitmap downloadBitmap(String src) {
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

        @Override
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            return downloadBitmap(params[0]);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            item.setBitmap(ByteBitmapConverter.getBytesFromBitmap(bitmap));
            downloadCallback.updateSingleImage(item);
        }
    }
}
