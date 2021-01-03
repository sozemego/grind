package com.soze.grind.core.game;

import com.google.common.eventbus.EventBus;
import com.soze.grind.core.game.event.ObjectSelectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Contains the currently selected object. */
@Service
public class SelectedObjectContainer {

  private final EventBus eventBus;

  private Object selectedObject;

  @Autowired
  public SelectedObjectContainer(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public Object getSelectedObject() {
    return selectedObject;
  }

  public void setSelectedObject(Object selectedObject) {
    this.selectedObject = selectedObject;
    this.eventBus.post(new ObjectSelectedEvent(selectedObject));
  }
}
