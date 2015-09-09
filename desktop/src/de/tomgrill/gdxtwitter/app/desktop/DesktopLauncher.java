package de.tomgrill.gdxtwitter.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.tomgrill.gdxtwitter.app.GdxTwitterSampleApp;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = 800;
		config.width = 600;
		new LwjglApplication(new GdxTwitterSampleApp(), config);
	}
}
