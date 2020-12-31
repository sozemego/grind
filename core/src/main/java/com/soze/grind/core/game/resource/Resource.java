package com.soze.grind.core.game.resource;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.storage.ResourceStorage;

/** Represents a resource in the world (tree, rock etc). */
public class Resource extends Actor {

  private final ResourceEnum resourceEnum;
  private final Texture texture;

  private final ResourceStorage storage;

  public Resource(Texture texture, ResourceEnum resourceEnum, ResourceStorage storage) {
    this.texture = texture;
    this.resourceEnum = resourceEnum;
    this.storage = storage;
  }

  public ResourceEnum getResourceEnum() {
    return resourceEnum;
  }

  /**
   * Gets the current available amount of the resource this object provides.
   */
  public int getAmountResource() {
    return this.storage.count(this.resourceEnum);
  }

  /**
   * Gets the maximum capacity of the resource this object provides.
   */
  public int getCapacity() {
    return this.storage.maxCapacity(this.resourceEnum);
  }

  /**
   * Removes up to a given amount of the resource this object provides and returns the actual amount removed.
   */
  public int removeResource(int amount) {
    return storage.removeResource(this.resourceEnum, amount);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
  }
}
