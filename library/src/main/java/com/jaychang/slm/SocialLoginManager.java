package com.jaychang.slm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import rx.Observable;
import rx.subjects.PublishSubject;

import static com.jaychang.slm.SocialLoginManager.SocialPlatform.FACEBOOK;
import static com.jaychang.slm.SocialLoginManager.SocialPlatform.GOOGLE;

public class SocialLoginManager {

  private static final String ERROR = "You must choose a social platform.";

  @SuppressLint("StaticFieldLeak")
  private static SocialLoginManager instance;
  private PublishSubject<SocialUser> userEmitter;
  private Context appContext;
  private boolean withProfile;
  private SocialPlatform socialPlatform;

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

  public SocialLoginManager facebook() {
    this.socialPlatform = FACEBOOK;
    return this;
  }

  public SocialLoginManager google() {
    this.socialPlatform = GOOGLE;
    return this;
  }

  public static void init(Application application) {
    FacebookSdk.sdkInitialize(application.getApplicationContext());
  }

  public Observable<SocialUser> login() {
    userEmitter = PublishSubject.create();
    Intent intent = new Intent(appContext, getLoginActivity());
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    appContext.startActivity(intent);
    return userEmitter;
  }

  private Class<?> getLoginActivity() {
    Class<?> clazz;
    if (socialPlatform == FACEBOOK) {
      clazz = FbLoginHiddenActivity.class;
    } else {
      throw new IllegalStateException(ERROR);
    }
    return clazz;
  }

  public void logout() {
    if (socialPlatform == FACEBOOK) {
      LoginManager.getInstance().logOut();
    }

    throw new IllegalStateException(ERROR);
  }

  public boolean isLogined() {
    if (socialPlatform == FACEBOOK) {
      return AccessToken.getCurrentAccessToken() != null;
    }

    throw new IllegalStateException(ERROR);
  }

  boolean isWithProfile() {
    return this.withProfile;
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

  enum SocialPlatform {
    FACEBOOK, GOOGLE
  }

}
