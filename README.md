# gdx-twitter-app
Example libGDX application implementing the gdx-twitter extension.


## Setting up your Twitter App
Go to https://apps.twitter.com/ and create a new app. 

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
