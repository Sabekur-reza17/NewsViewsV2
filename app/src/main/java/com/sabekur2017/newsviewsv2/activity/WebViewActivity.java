package com.sabekur2017.newsviewsv2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.sabekur2017.newsviewsv2.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView=findViewById(R.id.web_view);
        Intent intent=getIntent();
        final  String webUrl=intent.getExtras().getString("url");
        webView.loadUrl(webUrl);
    }
}
