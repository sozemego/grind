package com.soze.grind.core.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.function.Supplier;

public class ProgressBar extends Actor {

	private final Texture background;
	private final Texture bar;
	private final Texture filling;

	private final Supplier<Float> progressSupplier;

	public ProgressBar(
			Texture background,
			Texture bar,
			Texture filling,
			Supplier<Float> progressSupplier
	) {
		this.background = background;
		this.bar = bar;
		this.filling = filling;
		this.progressSupplier = progressSupplier;
	}

	public float getProgress() {
		return this.progressSupplier.get();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();

		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		batch.draw(background, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		batch.draw(filling, this.getX(), this.getY(), MathUtils.round(this.getWidth() * this.getProgress()), this.getHeight());
		batch.draw(bar, this.getX(), this.getY(), this.getWidth(), this.getHeight());
	}
}
