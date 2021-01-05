package com.soze.grind.core.game;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.soze.grind.core.game.ecs.component.ActorComponent;
import java.util.Objects;

/** Represents a marker that shows which object is selected. */
public class SelectedObjectMarker extends Actor {

  private final Texture texture;
  private final SelectedObjectContainer selectedObjectContainer;
  private boolean justSelectedObject;

  public SelectedObjectMarker(Texture texture, SelectedObjectContainer selectedObjectContainer) {
    this.texture = texture;
    this.selectedObjectContainer = selectedObjectContainer;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Actor actor = getActor();

    if (Objects.isNull(actor)) {
      justSelectedObject = false;
    }

    if (Objects.nonNull(actor)) {

      if (!justSelectedObject) {
        justSelectedObject = true;

        setWidth(actor.getWidth());
        setHeight(actor.getHeight());

        float xLower = getWidth() * 0.95f;
        float xHigher = getWidth() * 1.05f;

        float yLower = getHeight() * 0.95f;
        float yHigher = getHeight() * 1.05f;

        clearActions();

        addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.sizeTo(xLower, yLower, 0.35f, Interpolation.bounce),
                    Actions.sizeTo(xHigher, yHigher, 0.35f, Interpolation.bounce))));
      }

      Color color = getColor();
      batch.setColor(color.r, color.g, color.b, color.a * parentAlpha * actor.getColor().a);

      float x = actor.getX() - ((getWidth() - actor.getWidth()) / 2);
      float y = actor.getY() - ((getHeight() - actor.getHeight()) / 2);

      batch.draw(texture, x, y, getWidth(), getHeight());

    }

  }

  private Actor getActor() {
    Object selectedObject = selectedObjectContainer.getSelectedObject();

    if (Objects.isNull(selectedObject)) {
      return null;
    }

    if (selectedObject instanceof Actor) {
      return (Actor) selectedObject;
    }

    if (selectedObject instanceof Entity) {

      Entity entity = (Entity) selectedObject;

      ActorComponent actorComponent = entity.getComponent(ActorComponent.class);

      return actorComponent.getActor();
    }

    return null;
  }
}
