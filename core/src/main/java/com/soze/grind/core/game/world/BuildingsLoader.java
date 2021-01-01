package com.soze.grind.core.game.world;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.building.Warehouse;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.storage.TotalCapacityResourceStorage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Loads buildings from whichever place it needs. */
//@Service
public class BuildingsLoader {

  private static final Logger LOG = LogManager.getLogger(BuildingsLoader.class);

  private final AssetService assetService;

  private final List<Actor> buildings = new ArrayList<>();

  @Autowired
  public BuildingsLoader(AssetService assetService) {
    this.assetService = assetService;
  }

  @PostConstruct
  public void setup() {
    LOG.info("Loading buildings");

    ResourceStorage storage = new TotalCapacityResourceStorage(50);

    Warehouse warehouse = new Warehouse(this.assetService.getTexture("medievalStructure_19.png"), storage);

    warehouse.setX(50 * 64);
    warehouse.setY(50 * 64);
    warehouse.setWidth(64);
    warehouse.setHeight(64);

    this.buildings.add(warehouse);
  }

  public List<Actor> getBuildings() {
    return this.buildings;
  }
}
