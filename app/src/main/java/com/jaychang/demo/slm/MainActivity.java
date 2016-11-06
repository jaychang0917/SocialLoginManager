package com.jaychang.demo.slm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.jaychang.demo.slm.R;
import com.jaychang.slm.SocialLoginManager;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button fbLoginButton = (Button) findViewById(R.id.fbLoginButton);
    fbLoginButton.setOnClickListener(view -> {
      loginByFacebook();
    });

    SignInButton googleLoginButton = (SignInButton) findViewById(R.id.googleLoginButton);
    googleLoginButton.setOnClickListener(view -> {
       loginByGoogle();
    });

  }

  private void loginByFacebook() {
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

  private void loginByGoogle() {
    SocialLoginManager.getInstance(this)
      .google(getString(R.string.default_web_client_id))
      .login()
      .subscribe(socialUser -> {
          Log.d(TAG, "userId: " + socialUser.userId);
          Log.d(TAG, "photoUrl: " + socialUser.photoUrl);
          Log.d(TAG, "accessToken: " + socialUser.accessToken);
          Log.d(TAG, "email: " + socialUser.profile.email);
          Log.d(TAG, "name: " + socialUser.profile.name);
        },
        error -> {
          Log.d(TAG, "error: " + error.getMessage());
        });
  }

}
