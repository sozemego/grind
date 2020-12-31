package com.soze.grind.core.game.resource;

public enum ResourceEnum {
  WOOD("Wood"),
  STONE("Stone"),
  IRON("Iron"),
  GOLD("Gold");

  private final String name;

  ResourceEnum(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getTextureName() {
    return "resource/" + this.getName().toLowerCase() + ".png";
  }
}
