package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;

public class NameComponent extends Component {

  private String name;

  public NameComponent() {}

  public NameComponent(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
