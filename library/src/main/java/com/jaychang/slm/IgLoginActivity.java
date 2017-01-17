package com.jaychang.slm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IgLoginActivity extends AppCompatActivity {

  public static final String EXTRA_CLIENT_ID = "EXTRA_CLIENT_ID";
  public static final String EXTRA_REDIRECT_URL = "EXTRA_REDIRECT_URL";
  private static final String API = "https://api.instagram.com/oauth/authorize/?client_id=%1$s&redirect_uri=%2$s&response_type=token";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String clientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
    final String redirectUrl = getIntent().getStringExtra(EXTRA_REDIRECT_URL);

    WebView webView = new WebView(this);
    webView.loadUrl(String.format(API, clientId, redirectUrl));
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(redirectUrl)) {
          if (url.contains("access_token")) {
            SocialUser user = new SocialUser();
            user.accessToken = url.split("#")[1].split("=")[1];
            SocialLoginManager.getInstance(IgLoginActivity.this).onLoginSuccess(user);
          } else {
            RuntimeException error = new RuntimeException("instagram login fail");
            SocialLoginManager.getInstance(IgLoginActivity.this).onLoginError(error);
          }
          finish();
        }
        return false;
      }
    });

    setContentView(webView);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    SocialLoginManager.getInstance(IgLoginActivity.this).onLoginCancel();
  }
}
