package com.jaychang.demo.slm;

import android.support.multidex.MultiDexApplication;

import com.jaychang.slm.SocialLoginManager;

public class App extends MultiDexApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    SocialLoginManager.init(this);
  }
}
