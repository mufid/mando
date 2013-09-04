package com.mando.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockActivity;

public class SettingsTwitterLogin extends SherlockActivity {
    // Constants
    public static final String TWITTER_TOKEN = "twittertoken";
    public static final String TWITTER_VERIFIER = "twitterverifier";
    public static final String MANDO_TWITTER_CALLBACKURL = "myapp://oauth";
    public static final String TWITTER_AUTH_URI = "auth_uri";
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_Light);
        setContentView(R.layout.activity_settings_twitterlogin);
        
        WebView webView = (WebView) findViewById(R.id.twitterlogin_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setTitle("Sambungkan Mando ke Twitter");
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean result = true;
                if (url != null && url.startsWith(MANDO_TWITTER_CALLBACKURL)) {
                    Uri uri = Uri.parse(url);
                    if (uri.getQueryParameter("denied") != null) {
                        setResult(RESULT_CANCELED);
                        finish();
                    } else {
                        String oauthToken = uri.getQueryParameter("oauth_token");
                        String oauthVerifier = uri.getQueryParameter("oauth_verifier");

                        Intent intent = getIntent();
                        intent.putExtra(TWITTER_TOKEN, oauthToken);
                        intent.putExtra(TWITTER_VERIFIER, oauthVerifier);

                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    result = super.shouldOverrideUrlLoading(view, url);
                }
                return result;
            }
        });
        webView.loadUrl(this.getIntent().getExtras().getString(TWITTER_AUTH_URI));

    }
}
