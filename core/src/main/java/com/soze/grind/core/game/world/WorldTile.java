package com.soze.grind.core.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class WorldTile extends Actor {

  private final Texture texture;

  public WorldTile(Texture texture) {
    this.texture = texture;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    batch.draw(texture, getX(), getY(), getWidth(), getHeight());
  }
}
