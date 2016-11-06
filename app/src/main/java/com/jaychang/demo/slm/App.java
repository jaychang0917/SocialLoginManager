package com.jaychang.demo.slm;

import android.app.Application;

import com.jaychang.slm.SocialLoginManager;

public class App extends Application{

  @Override
  public void onCreate() {
    super.onCreate();
    SocialLoginManager.init(this);
  }
}
