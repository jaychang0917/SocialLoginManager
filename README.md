#SocialLoginManager

This library aims to eliminate boilerplate code for social login.

##Setup
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
    compile 'com.github.jaychang0917:SocialLoginManager:1.0.0'
}
```

##Usage
```java
public class App extends MultiDexApplication {

  @Override
  public void onCreate() {
    super.onCreate();
    SocialLoginManager.init(this);
  }
}
```
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

```
