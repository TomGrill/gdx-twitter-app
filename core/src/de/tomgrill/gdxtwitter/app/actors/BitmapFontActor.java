package de.tomgrill.gdxtwitter.app.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class BitmapFontActor extends Actor implements Disposable {

	private BitmapFont font;
	private String text = "";

	public BitmapFontActor(BitmapFont font) {
		this.font = font;

	}

	public void setText(String text) {
		if (!text.equals(this.text)) {
			this.text = text;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.drawMultiLine(batch, text, getX(), getY());
	}

	@Override
	public void dispose() {
		font.dispose();

	}

	@Override
	public float getWidth() {
		return font.getBounds(text).width;
	}

}
