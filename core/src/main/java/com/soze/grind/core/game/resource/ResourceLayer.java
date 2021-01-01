package com.soze.grind.core.game.resource;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.soze.grind.core.game.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ResourceLayer extends Group {

  private final List<Resource> resources = new ArrayList<>();

  public ResourceLayer(List<Resource> resources) {
    this.resources.addAll(resources);
    this.resources.forEach(this::addActor);
  }

  public void animateEnterWorld(float delay) {
    this.resources.forEach(
        resource -> {
          if (!resource.hasActions()) {
            AnimationUtils.resourceEntersWorld(resource, delay);
          }
        });
  }

  public List<Resource> getResources() {
    return resources;
  }
}
