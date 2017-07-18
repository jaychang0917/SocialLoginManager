# PLEASE NOTE, THIS PROJECT IS NO LONGER BEING MAINTAINED
Please check out [SimpleAuth](https://github.com/jaychang0917/SimpleAuth)

# SocialLoginManager
[![Release](https://jitpack.io/v/jaychang0917/SocialLoginManager.svg)](https://jitpack.io/#jaychang0917/SocialLoginManager)

This library aims to eliminate boilerplate code for social login.

## Setup
To use this library your minSdkVersion must be >= 15.

In your project level build.gradle :

```java
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

In your app level build.gradle :

```java
dependencies {
    compile 'com.github.jaychang0917:SocialLoginManager:{latest_version}'
}
```
[![Release](https://jitpack.io/v/jaychang0917/SocialLoginManager.svg)](https://jitpack.io/#jaychang0917/SocialLoginManager)

## Usage

## Step 0
**You must setup your `Manifest.xml` for facebook / google login to use this libary.**

For example:
#### Facebook login
```xml
<application
    ...
    <meta-data
        android:name="com.facebook.sdk.ApplicationId"
        android:value="@string/facebook_app_id" />

    <activity
        android:name="com.facebook.FacebookActivity"
        android:configChanges=
            "keyboard|keyboardHidden|screenLayout|screenSize|orientation"
        android:label="@string/app_name"
        android:theme="@android:style/Theme.Translucent.NoTitleBar"
        tools:replace="android:theme" />

    <activity
        android:name="com.facebook.CustomTabActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />

            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />

            <data android:scheme="@string/fb_login_protocol_scheme" />
        </intent-filter>
    </activity>
</application>
```
#### Google Login
- Put your `google-services.json` under `app` directory.
- Add `classpath 'com.google.gms:google-services:3.0.0'` in your project level build.gralde.
- Add `apply plugin: 'com.google.gms.google-services'` in your app level build.gralde

## Step 1

```java
public class App extends MultiDexApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    SocialLoginManager.init(this);
  }
}
```

## Step 2
```java
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button fbLoginButton = (Button) findViewById(R.id.fbLoginButton);
    fbLoginButton.setOnClickListener(view -> {
      loginByFacebook();
    });

    Button googleLoginButton = (Button) findViewById(R.id.googleLoginButton);
    googleLoginButton.setOnClickListener(view -> {
       loginByGoogle();
    });

  }

  private void loginByFacebook() {
    SocialLoginManager.getInstance(this)
      .facebook()
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
  
  private void loginByInstagram() {
    SocialLoginManager.getInstance(this)
      .instagram("client_id", "client_secret", "redirect_url")
      .login()
      .subscribe(socialUser -> {
        Log.d(TAG, "userId: " + socialUser.userId);
        Log.d(TAG, "photoUrl: " + socialUser.photoUrl);
        Log.d(TAG, "accessToken: " + socialUser.accessToken);
        Log.d(TAG, "name: " + socialUser.profile.name);
        Log.d(TAG, "fullName: " + socialUser.profile.fullName);
      }, error -> {
        Log.d(TAG, "error: " + error.getMessage());
      });
  }

}

```

## Change Log
[Change Log](https://github.com/jaychang0917/SocialLoginManager/blob/master/CHANGELOG.md)

## License
```
Copyright 2016 Jay Chang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
