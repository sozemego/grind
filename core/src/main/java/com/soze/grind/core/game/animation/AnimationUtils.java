package com.soze.grind.core.game.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.soze.grind.core.game.resource.Resource;
import com.soze.grind.core.game.world.WorldTile;

public class AnimationUtils {

  /**
   * Attaches actions to a world tile after it enters the world.
   *
   * @param tile tile
   * @param delay delay in seconds
   */
  public static void tileEntersWorld(WorldTile tile, float delay) {

    tile.clearActions();

    float x = tile.getX();
    float y = tile.getY();

    tile.setY(y + MathUtils.random(0, 15f));
    tile.setX(x + MathUtils.random(-5f, 5f));

    Color color = tile.getColor();
    tile.setColor(color.cpy().set(color.r, color.g, color.b, 0));

    SequenceAction sequence =
        Actions.sequence(
            Actions.delay(delay),
            Actions.parallel(
                Actions.moveTo(x, y, MathUtils.random(0.5f, 1.5f)),
                Actions.fadeIn(MathUtils.random(0.5f, 1.5f))));

    tile.addAction(sequence);
  }

  /**
   * Attaches actions when a building enters the world
   *
   * @param building building
   * @param delay delay
   */
  public static void buildingEntersWorld(Actor building, float delay) {

    building.clearActions();

    float x = building.getX();
    float y = building.getY();

    building.setY(y + MathUtils.random(0, 15f));

    Color color = building.getColor();
    building.setColor(color.cpy().set(color.r, color.g, color.b, 0.0f));

    SequenceAction sequence =
        Actions.sequence(
            Actions.delay(delay),
            Actions.parallel(
                Actions.moveTo(x, y, MathUtils.random(0.5f, 1.5f)),
                Actions.fadeIn(MathUtils.random(0.5f, 1.5f))));

    building.addAction(sequence);
  }

  /**
   * Sets up actions for when a resource enters the world
   *
   * @param resource resource
   * @param delay delay
   */
  public static void resourceEntersWorld(Resource resource, float delay) {

    resource.clearActions();

    float x = resource.getX();
    float y = resource.getY();

    resource.setY(y + MathUtils.random(0, 15f));

    Color color = resource.getColor();
    resource.setColor(color.cpy().set(color.r, color.g, color.b, 0.0f));

    SequenceAction sequence =
        Actions.sequence(
            Actions.delay(delay),
            Actions.parallel(
                Actions.moveTo(x, y, MathUtils.random(0.5f, 1.5f)),
                Actions.fadeIn(MathUtils.random(0.5f, 1.5f))));

    resource.addAction(sequence);
  }
}
