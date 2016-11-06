package com.jaychang.slm;

import rx.Observable;

public interface LoginContract {
  Observable<SocialUser> login();
  void logout();
}
