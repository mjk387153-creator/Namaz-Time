package com.namaz.clock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    WebView w;
    ValueCallback<Uri[]> cb;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        w = new WebView(this);
        setContentView(w);
        WebSettings s = w.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        w.setWebViewClient(new WebViewClient());
        w.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> c, FileChooserParams p) {
                cb = c;
                try {
                    startActivityForResult(p.createIntent(), 1);
                } catch (Exception e) {
                    cb = null;
                    return false;
                }
                return true;
            }
        });
        w.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onActivityResult(int r, int c, Intent d) {
        if (r == 1 && cb != null) {
            cb.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(c, d));
            cb = null;
        }
    }
}
