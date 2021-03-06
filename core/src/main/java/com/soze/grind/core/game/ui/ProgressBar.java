package com.soze.grind.core.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressBar extends Actor {

	private final Texture background;
	private final Texture bar;
	private final Texture filling;

	private float progress;

	public ProgressBar(
			Texture background,
			Texture bar,
			Texture filling
	) {
		this.background = background;
		this.bar = bar;
		this.filling = filling;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public float getProgress() {
		return progress;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		batch.draw(background, getX(), getY(), getWidth(), getHeight());
		batch.draw(filling, getX(), getY(), MathUtils.round(getWidth() * getProgress()), getHeight());
		batch.draw(bar, getX(), getY(), getWidth(), getHeight());
	}
}
