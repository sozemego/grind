package com.soze.grind.core.game.loader;

import com.artemis.World;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.soze.grind.core.game.Dungeon;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.ecs.component.factory.ComponentFactory;
import com.soze.grind.core.game.util.JsonUtil;
import com.soze.grind.core.game.world.WorldTile;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LevelLoader {

  private static final Logger LOG = LogManager.getLogger(LevelLoader.class);

  private final World world;
  private final ComponentFactory componentFactory;

  private final AssetService assetService;
  private final String currentLevelName;

  private final List<WorldTile> worldTiles = new ArrayList<>();
  private final List<Dungeon> dungeons = new ArrayList<>();

  @Autowired
  public LevelLoader(
      World world,
      ComponentFactory componentFactory,
      AssetService assetService,
      @Value("${currentLevelName}") String currentLevelName
  ) {
    this.world = world;
    this.componentFactory = componentFactory;
    this.assetService = assetService;
    this.currentLevelName = currentLevelName;
  }

  @PostConstruct
  public void setup() {
    loadLevel(currentLevelName);
  }

  public List<WorldTile> getWorldTiles() {
    return worldTiles;
  }


  public List<Dungeon> getDungeons() {
    return dungeons;
  }

  /**
   * Loads the level data with given level name.
   *
   * @param levelName levelName
   */
  public void loadLevel(String levelName) {
    LOG.info("Loading level = [{}]", levelName);

    JsonNode jsonNode = loadLevelData(levelName);

    loadTiles(jsonNode);
    loadEntities(jsonNode);
    loadDungeons(jsonNode);

    LOG.info("Level [{}] loaded.", levelName);
  }

  private void loadTiles(JsonNode jsonNode) {
    JsonNode world = jsonNode.get("world");

    int width = world.get("width").asInt();
    int height = world.get("height").asInt();

    LOG.info("Creating world with size = [{}, {}]", width, height);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        WorldTile worldTile = new WorldTile(assetService.getTexture("medievalTile_57.png"));
        worldTile.setPosition(i * 64, j * 64);
        worldTile.setSize(64, 64);
        worldTiles.add(worldTile);
      }
    }

    LOG.info("Created [{}] tiles", worldTiles.size());
  }

  /**
   * Loads all game entities.
   *
   * @param jsonNode jsonNode
   */
  private void loadEntities(JsonNode jsonNode) {
    LOG.info("Loading entities");

    ArrayNode entitiesNode = jsonNode.withArray("entities");

    for (JsonNode node : entitiesNode) {

      EntityLoader entityLoader = new EntityLoader(world, node, componentFactory);
      entityLoader.loadEntity();

    }

    LOG.info("Loaded [{}] entities", entitiesNode.size());
  }

  /**
   * Loads Dungeons.
   *
   * @param jsonNode data
   */
  private void loadDungeons(JsonNode jsonNode) {
    LOG.info("Loading dungeons.");

    dungeons.add(new Dungeon("Easy dungeon", "church.png"));
    dungeons.add(new Dungeon("Medium dungeon", "house.png"));
    dungeons.add(new Dungeon("Hard dungeon", "tower.png"));

    LOG.info("Loaded [{}] dungeons", dungeons.size());
  }

  /**
   * Loads level data from levels file.
   *
   * @param levelName name of the level to load
   * @return data only for the given level
   */
  private JsonNode loadLevelData(String levelName) {
    JsonNode levelsData = JsonUtil.loadJson("data/levels.json");
    return levelsData.get(levelName);
  }

}
