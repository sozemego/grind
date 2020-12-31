package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import java.util.Objects;
import org.springframework.stereotype.Service;

/** Represents a marker that shows which object is selected. */
@Service
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
    Actor selectedObject = this.selectedObjectContainer.getSelectedObject();

    if (Objects.isNull(selectedObject)) {
      this.justSelectedObject = false;
    }

    if (Objects.nonNull(selectedObject)) {

      if (!this.justSelectedObject) {
        this.justSelectedObject = true;

        this.setWidth(selectedObject.getWidth());
        this.setHeight(selectedObject.getHeight());

        float xLower = this.getWidth() * 0.95f;
        float xHigher = this.getWidth() * 1.05f;

        float yLower = this.getHeight() * 0.95f;
        float yHigher = this.getHeight() * 1.05f;

        this.clearActions();

        this.addAction(
            Actions.forever(
                Actions.sequence(
                    Actions.sizeTo(xLower, yLower, 0.35f, Interpolation.bounce),
                    Actions.sizeTo(xHigher, yHigher, 0.35f, Interpolation.bounce))));
      }

      Color color = getColor();
      batch.setColor(
          color.r, color.g, color.b, color.a * parentAlpha * selectedObject.getColor().a);

      float x = selectedObject.getX() - ((getWidth() - selectedObject.getWidth()) / 2);
      float y = selectedObject.getY() - ((getHeight() - selectedObject.getHeight()) / 2);

      batch.draw(texture, x, y, this.getWidth(), this.getHeight());
    }
  }
}
