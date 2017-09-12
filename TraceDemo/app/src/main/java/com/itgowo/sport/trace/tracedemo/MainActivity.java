package com.itgowo.sport.trace.tracedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itgowo.sport.trace.tracedemo.Trace.TraceMainActivity;

public class MainActivity extends AppCompatActivity {
private  WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          mWebView= (WebView) findViewById(R.id.webview);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://itgowo.com:8081/TraceServer");
    }
    public void onClick(View mView){
        TraceMainActivity.StartActivity(this);
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.destroy();
        super.onDestroy();


    }
}
