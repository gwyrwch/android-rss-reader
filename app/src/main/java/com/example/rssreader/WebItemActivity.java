package com.example.rssreader;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class WebItemActivity extends AppCompatActivity {
    public static final String ITEM_LINK = "link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webitem);

        String unencodedHtml = getIntent().getStringExtra(ITEM_LINK);


        WebView myWebView = findViewById(R.id.item_webview);

        Log.d("url", unencodedHtml);

        String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                Base64.NO_PADDING);

        Log.d("url2", encodedHtml);
        WebSettings webSetting = myWebView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(false);
        myWebView.setWebViewClient(new WebViewClient());

        myWebView.loadData(encodedHtml, "text/html", "base64");


//        myWebView.loadUrl(url);


//        finish();
    }

}


//    File file = new File(this.getExternalFilesDir(null), "fileName.html");
//        try {
//                FileUtils.copyURLToFile(new URL(url), file);
//                } catch (IOException e) {
//                e.printStackTrace();
//                }
//
//
//// loading saved file in webview
//                myWebView.loadUrl("file://" + file.getPath());
