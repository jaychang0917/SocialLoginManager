package com.jaychang.slm;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IgLoginActivity extends AppCompatActivity {

  public static final String EXTRA_CLIENT_ID = "EXTRA_CLIENT_ID";
  public static final String EXTRA_REDIRECT_URL = "EXTRA_REDIRECT_URL";
  public static final String EXTRA_CLIENT_SECRET = "EXTRA_CLIENT_SECRET";
  private static final String GET_CODE_URL = "https://api.instagram.com/oauth/authorize/?client_id=%1$s&redirect_uri=%2$s&response_type=code";
  private static final String GET_TOKEN_URL = "https://api.instagram.com/oauth/access_token";
  private String clientId;
  private String clientSecret;
  private String redirectUrl;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    clientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
    clientSecret = getIntent().getStringExtra(EXTRA_CLIENT_SECRET);
    redirectUrl = getIntent().getStringExtra(EXTRA_REDIRECT_URL);

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage(getString(R.string.slm_loading));

    WebView webView = new WebView(this);
    webView.loadUrl(String.format(GET_CODE_URL, clientId, redirectUrl));
    webView.setWebViewClient(new WebViewClient() {

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        progressDialog.show();
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressDialog.dismiss();
      }

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(redirectUrl)) {
          Uri uri = Uri.parse(url);
          boolean error = !TextUtils.isEmpty(uri.getQueryParameter("error"));
          if (error) {
            SocialLoginManager.getInstance(IgLoginActivity.this).onLoginError(new RuntimeException("instagram login fail"));
            finish();
          }

          boolean hasCode = !TextUtils.isEmpty(uri.getQueryParameter("code"));
          if (hasCode) {
            getUserResponse(url);
          }
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

  private void getUserResponse(String codeUrl) {
    Uri uri = Uri.parse(codeUrl);
    String code = uri.getQueryParameter("code");

    RequestBody formBody = new FormBody.Builder()
      .add("client_id", clientId)
      .add("client_secret", clientSecret)
      .add("grant_type", "authorization_code")
      .add("redirect_uri", redirectUrl)
      .add("code", code)
      .build();

    Request request = new Request.Builder().post(formBody)
      .url(GET_TOKEN_URL)
      .build();


    progressDialog.show();

    new OkHttpClient().newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            progressDialog.dismiss();
            SocialLoginManager.getInstance(IgLoginActivity.this).onLoginError(new RuntimeException("instagram login fail"));
            finish();
          }
        });
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              SocialLoginManager.getInstance(IgLoginActivity.this).onLoginError(new RuntimeException("instagram login fail"));
              progressDialog.dismiss();
              finish();
            }
          });
          return;
        }

        String body = response.body().string();

        IgUser igUser = new Gson().fromJson(body, IgUser.class);

        final SocialUser user = new SocialUser();
        user.accessToken = igUser.accessToken;
        user.userId = igUser.user.id;
        SocialUser.Profile profile = new SocialUser.Profile();
        profile.name = igUser.user.username;
        profile.fullName = igUser.user.fullName;
        user.photoUrl = igUser.user.profilePicture;
        user.profile = profile;

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            SocialLoginManager.getInstance(IgLoginActivity.this).onLoginSuccess(user);
            progressDialog.dismiss();
            finish();
          }
        });
      }
    });
  }

  public static class IgUser {
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("user")
    public User user;

    public static class User {
      @SerializedName("id")
      public String id;
      @SerializedName("username")
      public String username;
      @SerializedName("full_name")
      public String fullName;
      @SerializedName("profile_picture")
      public String profilePicture;
    }
  }

}
