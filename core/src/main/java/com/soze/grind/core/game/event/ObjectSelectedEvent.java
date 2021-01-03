package com.soze.grind.core.game.event;

public class ObjectSelectedEvent {

  public final Object selectedObject;

  public ObjectSelectedEvent(Object selectedObject) {
    this.selectedObject = selectedObject;
  }
}
