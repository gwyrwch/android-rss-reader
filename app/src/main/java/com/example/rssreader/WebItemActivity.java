package com.example.rssreader;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


public class WebItemActivity extends AppCompatActivity {
    public static final String ITEM_LINK = "link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webitem);

        String url = getIntent().getStringExtra(ITEM_LINK);

        WebView myWebView = findViewById(R.id.item_webview);

        Log.d("url", url);

        myWebView.loadUrl(url);

        finish();
    }

}
