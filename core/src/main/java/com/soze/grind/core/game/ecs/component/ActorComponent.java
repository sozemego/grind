package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComponent extends Component {

  private Actor actor;

  public ActorComponent() {}

  public ActorComponent(Actor actor) {
    this.actor = actor;
  }

  public Actor getActor() {
    return actor;
  }

  public void setActor(Actor actor) {
    this.actor = actor;
    this.actor.setSize(64, 64);
  }
}
