package com.soze.grind.core.game.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.soze.grind.core.game.GameStage;
import com.soze.grind.core.game.SelectedObjectMarker;
import com.soze.grind.core.game.resource.ResourceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyWorld extends Group {

  private final TileLayer tileLayer;
  private final ResourceLayer resourceLayer;

  @Autowired
  public MyWorld(
      TileLayer tileLayer,
      ResourceLayer resourceLayer,
      SelectedObjectMarker selectedObjectMarker,
      GameStage gameStage
  ) {
    this.tileLayer = tileLayer;
    this.resourceLayer = resourceLayer;

    this.addActor(tileLayer);
    this.addActor(selectedObjectMarker);
    this.addActor(resourceLayer);

    gameStage.addActor(this);
  }

  public void animateEnterWorld(Camera camera) {
    this.tileLayer.animateEnterWorld(camera);
//    this.resourceLayer.animateEnterWorld(2f);
  }

  public TileLayer getTiles() {
    return tileLayer;
  }

  public ResourceLayer getResourceLayer() {
    return resourceLayer;
  }
}
