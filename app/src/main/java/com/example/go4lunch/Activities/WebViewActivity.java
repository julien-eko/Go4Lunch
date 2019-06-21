package com.example.go4lunch.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;


//Open website of restaurant
public class WebViewActivity extends AppCompatActivity {


    @BindView(R.id.activity_main_webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        webView.loadUrl(getIntent().getStringExtra("url"));
    }


}
