package de.tomgrill.gdxtwitter.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.tomgrill.gdxtwitter.core.ResponseListener;
import de.tomgrill.gdxtwitter.core.TwitterAPI;
import de.tomgrill.gdxtwitter.core.TwitterSystem;

public class GdxTwitterSampleApp extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private TwitterAPI twitterAPI;

	@Override
	public void create() {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		TwitterSystem twitterSystem = new TwitterSystem(new MyTwitterConfig());

		twitterAPI = twitterSystem.getTwitterAPI();

		twitterAPI.signin(true, new ResponseListener() {

			@Override
			public void success() {

				System.out.println("LOGIN SUCCESS");
				System.out.println(twitterAPI.getUserToken());
				System.out.println(twitterAPI.getUserTokenSecret());

			}

			@Override
			public void error(String errorMsg) {
				System.out.println("LOGIN ERROR" + errorMsg);

			}

			@Override
			public void cancel() {
				System.out.println("LOGIN CANCEL");

			}
		});

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}
