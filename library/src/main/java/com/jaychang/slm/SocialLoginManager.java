package com.jaychang.slm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SocialLoginManager {

  @SuppressLint("StaticFieldLeak")
  private static SocialLoginManager instance;
  private PublishSubject<SocialUser> userEmitter;
  private Context appContext;
  private boolean withProfile;

  private SocialLoginManager(Context context) {
    appContext = context;
  }

  public static synchronized SocialLoginManager getInstance(Context context) {
    if (instance == null) {
      instance = new SocialLoginManager(context);
    }
    return instance;
  }

  public SocialLoginManager withProfile() {
    this.withProfile = true;
    return this;
  }

  boolean isWithProfile() {
    return this.withProfile;
  }

  public static void init(Application application) {
    FacebookSdk.sdkInitialize(application.getApplicationContext());
  }

  public Observable<SocialUser> login() {
    userEmitter = PublishSubject.create();
    Intent intent = new Intent(appContext, HiddenActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    appContext.startActivity(intent);
    return userEmitter;
  }

  public void logout() {
    LoginManager.getInstance().logOut();
  }

  public boolean isLogined() {
    return AccessToken.getCurrentAccessToken() != null;
  }

  void onLoginSuccess(SocialUser socialUser) {
    if (userEmitter != null) {
      SocialUser copy = new SocialUser(socialUser);
      userEmitter.onNext(copy);
      userEmitter.onCompleted();
    }
  }

  void onLoginError(Throwable throwable) {
    if (userEmitter != null) {
      Throwable copy = new Throwable(throwable);
      userEmitter.onError(copy);
    }
  }

}
