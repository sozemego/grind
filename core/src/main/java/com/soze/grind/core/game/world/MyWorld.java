package com.soze.grind.core.game.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.soze.grind.core.game.GameStage;
import com.soze.grind.core.game.SelectedObjectMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyWorld extends Group {

  private final TileLayer tileLayer;

  @Autowired
  public MyWorld(
      TileLayer tileLayer, SelectedObjectMarker selectedObjectMarker, GameStage gameStage) {
    this.tileLayer = tileLayer;

    this.addActor(tileLayer);
    this.addActor(selectedObjectMarker);

    gameStage.addActor(this);
  }

  public void animateEnterWorld(Camera camera) {
    this.tileLayer.animateEnterWorld(camera);
  }

  public TileLayer getTiles() {
    return tileLayer;
  }
}
