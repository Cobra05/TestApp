package com.example.testapp;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class a_webView extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_webview);

        webView = findViewById(R.id.webView);
        String link = String.valueOf(getIntent().getSerializableExtra("LINK"));
        webView.loadUrl(link);

    }
}
