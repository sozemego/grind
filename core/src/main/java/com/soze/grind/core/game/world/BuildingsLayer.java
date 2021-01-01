package com.soze.grind.core.game.world;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.soze.grind.core.game.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BuildingsLayer extends Group {

  private final List<Actor> buildings = new ArrayList<>();

  public BuildingsLayer(List<Actor> buildings) {
    this.buildings.addAll(buildings);
    this.buildings.forEach(this::addActor);
  }

  public void animateEnterWorld(float delay) {
    this.buildings.forEach(
        building -> {
          if (!building.hasActions()) {
            AnimationUtils.buildingEntersWorld(building, delay);
          }
        });
  }

  public List<Actor> getBuildings() {
    return buildings;
  }
}
