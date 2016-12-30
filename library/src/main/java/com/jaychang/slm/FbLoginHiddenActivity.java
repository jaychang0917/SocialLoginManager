package com.jaychang.slm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class FbLoginHiddenActivity extends AppCompatActivity
  implements FacebookCallback<LoginResult>, GraphRequest.GraphJSONObjectCallback {

  private static final String PHOTO_URL = "https://graph.facebook.com/%1$s/picture?type=large";
  private static final List<String> PERMISSIONS = Arrays.asList("email", "public_profile");
  private CallbackManager callbackManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    callbackManager = CallbackManager.Factory.create();
    LoginManager.getInstance().registerCallback(callbackManager, this);

    LoginManager.getInstance().logOut();

    if (SocialLoginManager.getInstance(this).isWithProfile()) {
      LoginManager.getInstance().logInWithReadPermissions(this, PERMISSIONS);
    } else {
      LoginManager.getInstance().logInWithReadPermissions(this, null);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void onSuccess(LoginResult loginResult) {
    if (!SocialLoginManager.getInstance(this).isWithProfile()) {
      handleLoginSuccess(loginResult);
      finish();
      return;
    }

    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), this);
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,email,link");
    request.setParameters(parameters);
    request.executeAsync();
  }

  private void handleLoginSuccess(LoginResult result) {
    SocialUser user = new SocialUser();
    user.userId = result.getAccessToken().getUserId();
    user.accessToken = result.getAccessToken().getToken();
    user.photoUrl = String.format(PHOTO_URL, user.userId);
    SocialLoginManager.getInstance(this).onLoginSuccess(user);
  }

  @Override
  public void onCancel() {
    SocialLoginManager.getInstance(this).onLoginCancel();
    finish();
  }

  @Override
  public void onError(FacebookException error) {
    SocialLoginManager.getInstance(this).onLoginError(error.getCause());
    finish();
  }

  @Override
  public void onCompleted(JSONObject object, GraphResponse response) {
    try {
      SocialUser user = new SocialUser();
      user.userId = object.getString("id");
      user.accessToken = AccessToken.getCurrentAccessToken().getToken();
      user.photoUrl = String.format(PHOTO_URL, user.userId);
      SocialUser.Profile profile = new SocialUser.Profile();
      profile.email = object.has("email")? object.getString("email") : "";
      profile.name = object.has("name")? object.getString("name"): "";
      profile.pageLink = object.has("link")? object.getString("link"): "";
      user.profile = profile;
      SocialLoginManager.getInstance(this).onLoginSuccess(user);
    } catch (JSONException e) {
      SocialLoginManager.getInstance(this).onLoginError(e.getCause());
    }

    LoginManager.getInstance().logOut();

    finish();
  }
}
