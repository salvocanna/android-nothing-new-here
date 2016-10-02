package com.firebox.androidapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.firebox.androidapp.R;

public class ActivityWebView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView w = (WebView) findViewById(R.id.webview);
        WebSettings s = w.getSettings();
        s.setJavaScriptEnabled(true);
        s.setJavaScriptCanOpenWindowsAutomatically(false);
        //s.set

        w.setWebViewClient(new WebViewClient());
        w.loadUrl("https://www.firebox.com");
    }
}
