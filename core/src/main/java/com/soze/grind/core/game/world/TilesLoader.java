package com.soze.grind.core.game.world;

import com.soze.grind.core.game.assets.AssetService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** A service which loads tiles from whatever place it needs to load them. */
//@Service
public class TilesLoader {

  private static final Logger LOG = LogManager.getLogger(TilesLoader.class);

  private final AssetService assetService;

  private final List<WorldTile> worldTiles = new ArrayList<>();

  @Autowired
  public TilesLoader(AssetService assetService) {
    this.assetService = assetService;
  }

  @PostConstruct
  public void setup() {
    LOG.info("Loading tiles");

    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 100; j++) {
        WorldTile worldTile = new WorldTile(this.assetService.getTexture("medievalTile_57.png"));
        worldTile.setPosition(i * 64, j * 64);
        worldTile.setSize(64, 64);
        worldTiles.add(worldTile);
      }
    }
  }

  public List<WorldTile> getTiles() {
    return this.worldTiles;
  }
}
