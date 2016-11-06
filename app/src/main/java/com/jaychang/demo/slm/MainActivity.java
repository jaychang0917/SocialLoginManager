package com.jaychang.demo.slm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.facebook.login.widget.LoginButton;
import com.jaychang.slm.SocialLoginManager;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button loginButton = (Button) findViewById(R.id.loginButton);

    loginButton.setOnClickListener(view -> {
      login();
    });

  }

  private void login() {
    SocialLoginManager.getInstance(this)
      .facebook()
      .withProfile()
      .login()
      .subscribe(socialUser -> {
          Log.d(TAG, "userId: " + socialUser.userId);
          Log.d(TAG, "photoUrl: " + socialUser.photoUrl);
          Log.d(TAG, "accessToken: " + socialUser.accessToken);
          Log.d(TAG, "name: " + socialUser.profile.name);
          Log.d(TAG, "email: " + socialUser.profile.email);
          Log.d(TAG, "pageLink: " + socialUser.profile.pageLink);
        },
        error -> {
          Log.d(TAG, "error: " + error.getMessage());
        });
  }

}
