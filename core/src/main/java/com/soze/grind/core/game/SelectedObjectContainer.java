package com.soze.grind.core.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.EventBus;
import com.soze.grind.core.game.event.ObjectSelectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Contains the currently selected object. */
@Service
public class SelectedObjectContainer {

  private final EventBus eventBus;

  private Actor selectedObject;

  @Autowired
  public SelectedObjectContainer(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public Actor getSelectedObject() {
    return selectedObject;
  }

  public void setSelectedObject(Actor selectedObject) {
    this.selectedObject = selectedObject;
    this.eventBus.post(new ObjectSelectedEvent(selectedObject));
  }
}
