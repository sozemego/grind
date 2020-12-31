package com.soze.grind.core.game.building;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.storage.ResourceStorage;
import java.util.Optional;

/**
 * A warehouse which stores resources.
 */
public class Warehouse extends Actor implements Building {

  private final Texture texture;
  private final ResourceStorage storage;

  public Warehouse(Texture texture, ResourceStorage storage) {
    this.texture = texture;
    this.storage = storage;
  }

  @Override
  public String getName() {
    return "Warehouse";
  }

  @Override
  public Optional<ResourceStorage> getResourceStorage() {
    return Optional.ofNullable(storage);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
  }
}
