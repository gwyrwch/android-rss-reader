package com.example.rssreader;

import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


public class WebItemActivity extends AppCompatActivity {
    public static final String ITEM_LINK = "link";
    public static final String ITEM_HTML = "html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webitem);

        WebView myWebView = findViewById(R.id.item_webview);

        String unencodedHtml = getIntent().getStringExtra(ITEM_HTML);
        String url = getIntent().getStringExtra(ITEM_LINK);

        if (url != null) {
            myWebView.loadUrl(url);

        } else if (unencodedHtml != null)  {
            String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(),
                    Base64.NO_PADDING);
            WebSettings webSetting = myWebView.getSettings();
            webSetting.setBuiltInZoomControls(true);
            webSetting.setJavaScriptEnabled(false);
            myWebView.setWebViewClient(new WebViewClient());

            myWebView.loadData(encodedHtml, "text/html", "base64");
        } else {
            finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        finish();
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
