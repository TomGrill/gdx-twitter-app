# gdx-twitter-app
Example libGDX application implementing the gdx-twitter extension.

## Updates & News
Follow me to receive release updates about this and my other projects (Promise: No BS posts)

https://twitter.com/TomGrillGames and https://www.facebook.com/tomgrillgames

I will also stream sometimes when developing at https://www.twitch.tv/tomgrill and write a blog article from time to time at http://tomgrill.de 

## Setting up your Twitter App
Go to https://apps.twitter.com/ and create a new app. 

Make sure to uncheck "Enable Callback Locking (It is recommended to enable callback locking to ensure apps cannot overwrite the callback url)" option in your app settings.

##Get it running

**General**
Import project to your IDE

1. Add new MyTwitterConfig class to core project. Replace TWITTER_CONSUMER_KEY_VALUE  and TWITTER_CONSUMER_SECRET_VALUE with your Consumer Key and Secret. 
```
public class MyTwitterConfig extends TwitterConfig {
	public MyTwitterConfig() {
		TWITTER_CONSUMER_KEY = "TWITTER_CONSUMER_KEY_VALUE";
		TWITTER_CONSUMER_SECRET = "TWITTER_CONSUMER_SECRET_VALUE";
	}
}
```

You should be good to go.
