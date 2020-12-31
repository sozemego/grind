package com.soze.grind.core.game.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.soze.grind.core.game.SelectedObjectContainer;
import com.soze.grind.core.game.SelectedObjectMarker;
import com.soze.grind.core.game.resource.ResourceLayer;
import com.soze.grind.core.game.unit.WorkerLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class World extends Group {

  private final TileLayer tileLayer;
  private final BuildingsLayer buildings;
  private final WorkerLayer workerLayer;
  private final ResourceLayer resourceLayer;
  private final SelectedObjectMarker selectedObjectMarker;
  private final SelectedObjectContainer selectedObjectContainer;

  @Autowired
  public World(
      TileLayer tileLayer,
      BuildingsLayer buildings,
      WorkerLayer workerLayer,
      ResourceLayer resourceLayer,
      SelectedObjectMarker selectedObjectMarker,
      SelectedObjectContainer selectedObjectContainer) {
    this.tileLayer = tileLayer;
    this.buildings = buildings;
    this.workerLayer = workerLayer;
    this.resourceLayer = resourceLayer;
    this.selectedObjectMarker = selectedObjectMarker;
    this.selectedObjectContainer = selectedObjectContainer;

    this.addActor(tileLayer);
    this.addActor(selectedObjectMarker);
    this.addActor(buildings);
    this.addActor(resourceLayer);
    this.addActor(workerLayer);
  }

  public void animateEnterWorld(Camera camera) {
    this.tileLayer.animateEnterWorld(camera);
    this.buildings.animateEnterWorld(1f);
//    this.resourceLayer.animateEnterWorld(2f);
  }

  public TileLayer getTiles() {
    return tileLayer;
  }

  public BuildingsLayer getBuildingsLayer() {
    return buildings;
  }

  public WorkerLayer getWorkerLayer() {
    return workerLayer;
  }

  public ResourceLayer getResourceLayer() {
    return resourceLayer;
  }
}
