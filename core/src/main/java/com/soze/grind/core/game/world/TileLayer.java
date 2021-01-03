package com.soze.grind.core.game.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.soze.grind.core.game.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.List;

public class TileLayer extends Group {

  private final List<WorldTile> worldTiles = new ArrayList<>();

  public TileLayer(List<WorldTile> worldTiles) {
    this.worldTiles.addAll(worldTiles);
    this.worldTiles.forEach(this::addActor);
  }

  /**
   * Animates the tiles when they first enter the world. The animation is not applied if the tile
   * has any actions.
   */
  public void animateEnterWorld(Camera camera) {

    float maxDelay = 2.5f;

    List<WorldTile> tiles = this.getSortedTiles(camera);

    for (int i = 0; i < tiles.size(); i++) {
      WorldTile tile = tiles.get(i);
      if (tile.hasActions()) {
        continue;
      }

      float percent = (float) i / worldTiles.size();

      AnimationUtils.tileEntersWorld(tile, percent * maxDelay);
    }
  }

  private List<WorldTile> getSortedTiles(Camera camera) {
    List<WorldTile> tiles = new ArrayList<>(this.worldTiles);

    float x = camera.position.x;
    float y = camera.position.y;

    Vector2 cameraVector = new Vector2(x, y);

    tiles.sort(
        (tile1, tile2) -> {
          float distance1 = new Vector2(tile1.getX(), tile1.getY()).dst2(cameraVector);
          float distance2 = new Vector2(tile2.getX(), tile2.getY()).dst2(cameraVector);

          return Float.compare(distance1, distance2);
        });

    return tiles;
  }
}
