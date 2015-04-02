package de.tomgrill.gdxtwitter.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.tomgrill.gdxtwitter.app.actors.BitmapFontActor;
import de.tomgrill.gdxtwitter.app.actors.ButtonActor;
import de.tomgrill.gdxtwitter.core.TwitterAPI;
import de.tomgrill.gdxtwitter.core.TwitterRequest;
import de.tomgrill.gdxtwitter.core.TwitterRequestType;
import de.tomgrill.gdxtwitter.core.TwitterResponseListener;
import de.tomgrill.gdxtwitter.core.TwitterSystem;

public class GdxTwitterSampleApp extends ApplicationAdapter {
	private static final String TAG = GdxTwitterSampleApp.class.getName();

	private static final String NOT_LOGGED_IN = "You are not logged in.";

	private Stage stage;
	private ButtonActor loginButton;
	private ButtonActor logoutKeepButton;
	private ButtonActor logoutDeleteButton;
	private ButtonActor tweetButton;
	private BitmapFontActor facebookFont;
	private BitmapFontActor autoLogin;

	private BitmapFontActor textBelow;
	private String textToTweet = "Awesome, I use https://github.com/TomGrill/gdx-twitter.";
	private String textToTweet2 = "Thanks @TomGrillGames";

	private String twitterNickname;
	private String twitterID;

	private CheckBox checkbox;

	private long lastRequest;

	Preferences prefs;

	private TwitterAPI twitterAPI;

	private MyTwitterConfig myConfig;

	private void tweetTheText() {

		TwitterRequest tweetTextRequest = new TwitterRequest(TwitterRequestType.POST, "https://api.twitter.com/1.1/statuses/update.json");
		tweetTextRequest.put("status", textToTweet + " " + textToTweet2);

		twitterAPI.newAPIRequest(tweetTextRequest, new TwitterResponseListener() {

			@Override
			public void cancelled() {
				System.out.println("cancelled");

			}

			@Override
			public void success(String data) {
				System.out.println("success: " + data);

			}

			@Override
			public void apiError(HttpStatus response, String data) {
				System.out.println("apiError " + data);

			}

			@Override
			public void httpError(Throwable t) {
				System.out.println("httpError" + t.getMessage());

			}
		});

	}

	@Override
	public void create() {

		myConfig = new MyTwitterConfig();

		prefs = Gdx.app.getPreferences("gdx-twitter-app-data");

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		TwitterSystem twitterSystem = new TwitterSystem(myConfig);

		twitterAPI = twitterSystem.getTwitterAPI();

		stage = new Stage(new ExtendViewport(640, 800, 640, 800));
		Gdx.input.setInputProcessor(stage);

		TwitterSystem facebookSystem = new TwitterSystem(myConfig);
		twitterAPI = facebookSystem.getTwitterAPI();

		tweetButton = new ButtonActor(new TextureRegion(new Texture("tweet-button.png")));
		tweetButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				tweetTheText();
			}

		});
		tweetButton.setX(640 / 2f - 251 / 2f);
		tweetButton.setY(400);

		tweetButton.setWidth(251);
		tweetButton.setHeight(28);
		tweetButton.setVisible(false);

		stage.addActor(tweetButton);

		loginButton = new ButtonActor(new TextureRegion(new Texture("sign-in-with-twitter-gray.png")));
		loginButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				handleGUITwitterSignin();
			}

		});
		loginButton.setX(640 / 2f - 158 / 2f);
		loginButton.setY(700);

		loginButton.setWidth(158);
		loginButton.setHeight(28);
		loginButton.setVisible(false);

		stage.addActor(loginButton);

		logoutKeepButton = new ButtonActor(new TextureRegion(new Texture("logout-keep.png")));
		logoutKeepButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				handleLogout(false);
			}

		});
		logoutKeepButton.setX(640 / 2f - 225 / 2f);
		logoutKeepButton.setY(700);

		logoutKeepButton.setWidth(225);
		logoutKeepButton.setHeight(40);
		logoutKeepButton.setVisible(false);

		stage.addActor(logoutKeepButton);

		logoutDeleteButton = new ButtonActor(new TextureRegion(new Texture("logout-delete.png")));
		logoutDeleteButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				handleLogout(true);
			}

		});
		logoutDeleteButton.setX(640 / 2f - 239 / 2f);
		logoutDeleteButton.setY(650);

		logoutDeleteButton.setWidth(239);
		logoutDeleteButton.setHeight(40);
		logoutDeleteButton.setVisible(false);

		stage.addActor(logoutDeleteButton);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Bold.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;
		parameter.color = Color.BLACK;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		facebookFont = new BitmapFontActor(font);
		facebookFont.setX(140);
		facebookFont.setY(560);
		facebookFont.setText("");
		stage.addActor(facebookFont);

		autoLogin = new BitmapFontActor(font);
		autoLogin.setX(235);
		autoLogin.setY(515);
		autoLogin.setText("autosignin on next restart");

		stage.addActor(autoLogin);

		CheckBoxStyle style = new CheckBoxStyle();
		style.font = font;
		style.checkboxOff = new TextureRegionDrawable(new TextureRegion(new Texture("checkbox_no.png")));
		style.checkboxOn = new TextureRegionDrawable(new TextureRegion(new Texture("checkbox_yes.png")));

		checkbox = new CheckBox("", style);
		checkbox.setX(200);
		checkbox.setY(500);
		checkbox.setHeight(240 * 0.1f);
		checkbox.setWidth(300 * 0.1f);

		if (prefs.getBoolean("autosignin", false)) {
			checkbox.setChecked(true);
		}

		checkbox.setVisible(true);
		stage.addActor(checkbox);

		textBelow = new BitmapFontActor(font);
		textBelow.setX(30);
		textBelow.setY(300);
		textBelow.setText(textToTweet + "\n" + textToTweet2);
		textBelow.setVisible(false);
		stage.addActor(textBelow);

		autoSignin();

		System.out.println("stored token: " + twitterAPI.getToken());
		System.out.println("stored token secret: " + twitterAPI.getTokenSecret());

	}

	private void autoSignin() {
		if (prefs.getBoolean("autosignin", false) && twitterAPI.isLoaded() && !twitterAPI.isSignedin()) {
			twitterAPI.signin(false, new TwitterResponseListener() {

				@Override
				public void success(String data) {
					Gdx.app.debug(TAG, "Autosignin successfull");

				}

				@Override
				public void apiError(HttpStatus response, String data) {
					Gdx.app.debug(TAG, "Autosignin with API error: " + data);

				}

				@Override
				public void httpError(Throwable t) {
					Gdx.app.debug(TAG, "Autosignin with http error: " + t.getMessage());

				}

				@Override
				public void cancelled() {
					Gdx.app.debug(TAG, "Autosignin canceled");

				}
			});
		}

	}

	private void handleLogout(boolean delete) {
		checkbox.setChecked(false);

		twitterAPI.signout(delete);
		twitterNickname = null;
		twitterID = null;

		lastRequest = 0;
	}

	private void handleGUITwitterSignin() {
		System.out.println("click login");

		twitterAPI.signin(true, new TwitterResponseListener() {

			@Override
			public void success(String data) {
				Gdx.app.debug(TAG, "GUI Signin successfull");

			}

			@Override
			public void apiError(HttpStatus response, String data) {
				Gdx.app.debug(TAG, "GUI Signin with error: " + data);

			}

			@Override
			public void httpError(Throwable t) {
				Gdx.app.debug(TAG, "GUI Signin httpError" + t.getMessage());

			}

			@Override
			public void cancelled() {
				Gdx.app.debug(TAG, "GUI Signin canceled");

			}
		});
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateText();
		updateButton();

		stage.draw();

	}

	private void updateText() {
		if (twitterAPI.isSignedin()) {
			if (twitterNickname == null && twitterID == null) {

				if (lastRequest + 10 < TimeUtils.millis() / 1000L) {

					lastRequest = TimeUtils.millis() / 1000L;

					TwitterRequest request = new TwitterRequest(TwitterRequestType.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");

					twitterAPI.newAPIRequest(request, new TwitterResponseListener() {

						@Override
						public void cancelled() {
							Gdx.app.log(TAG, "Request cancelled.");

						}

						@Override
						public void success(String data) {
							String result = data;

							Gdx.app.log(TAG, "Request successfull: Responsebody: " + result);

							JsonValue root = new JsonReader().parse(result);

							twitterID = root.getString("id");
							twitterNickname = root.getString("name");

							Gdx.app.log(TAG, "Name: " + twitterNickname);
							Gdx.app.log(TAG, "ID: " + twitterID);

						}

						@Override
						public void apiError(HttpStatus response, String data) {
							Gdx.app.log(TAG, "Request with error: \n" + data);
							handleLogout(true);

						}

						@Override
						public void httpError(Throwable t) {
							Gdx.app.log(TAG, "Request failed. httpError: " + t.getMessage());
						}
					});

				}
			}

			if (twitterNickname != null && twitterID != null) {
				facebookFont.setText("Hello " + twitterNickname + ", your unique ID is: " + twitterID);
				facebookFont.setX(640 / 2f - facebookFont.getWidth() / 2f);
			}
		} else {
			facebookFont.setText(NOT_LOGGED_IN);
			facebookFont.setX(640 / 2f - facebookFont.getWidth() / 2f);
		}

	}

	private void updateButton() {
		if (twitterAPI.isSignedin()) {
			loginButton.setVisible(false);
			logoutKeepButton.setVisible(true);
			logoutDeleteButton.setVisible(true);
			tweetButton.setVisible(true);
			textBelow.setVisible(true);
		} else {
			loginButton.setVisible(true);
			logoutKeepButton.setVisible(false);
			logoutDeleteButton.setVisible(false);
			tweetButton.setVisible(false);
			textBelow.setVisible(false);
		}

	}

	@Override
	public void pause() {
		super.pause();

		if (checkbox.isChecked()) {
			prefs.putBoolean("autosignin", true);
		} else {

			prefs.putBoolean("autosignin", false);
		}

		prefs.flush();
	}

	@Override
	public void dispose() {
		stage.dispose();
		facebookFont.dispose();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
