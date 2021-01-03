package com.soze.grind.core.game.loader;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.ecs.component.ActorComponent;
import com.soze.grind.core.game.ecs.component.BuildingComponent;
import com.soze.grind.core.game.ecs.component.ComponentFactory;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.PositionComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.component.WarehouseComponent;
import com.soze.grind.core.game.resource.Resource;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.storage.TotalCapacityResourceStorage;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import com.soze.grind.core.game.unit.Worker;
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
  private final UIElementFactory uiElementFactory;
  private final String currentLevelName;

  private final List<WorldTile> worldTiles = new ArrayList<>();
  private final List<Resource> resources = new ArrayList<>();

  @Autowired
  public LevelLoader(
      World world,
      ComponentFactory componentFactory,
      AssetService assetService,
      UIElementFactory uiElementFactory,
      @Value("${currentLevelName}") String currentLevelName
  ) {
    this.world = world;
    this.componentFactory = componentFactory;
    this.assetService = assetService;
    this.uiElementFactory = uiElementFactory;
    this.currentLevelName = currentLevelName;
  }

  @PostConstruct
  public void setup() {
    this.loadLevel(this.currentLevelName);
  }

  public List<WorldTile> getWorldTiles() {
    return worldTiles;
  }

  public List<Resource> getResources() {
    return resources;
  }

  /**
   * Loads the level data with given level name.
   *
   * @param levelName levelName
   */
  public void loadLevel(String levelName) {
    LOG.info("Loading level = [{}]", levelName);

    JsonNode jsonNode = this.loadLevelData(levelName);

    this.loadTiles(jsonNode);
    this.loadResources(jsonNode);
    this.loadWorkers(jsonNode);
    this.loadBuildings(jsonNode);

    LOG.info("Level [{}] loaded.", levelName);
  }

  private void loadTiles(JsonNode jsonNode) {
    JsonNode world = jsonNode.get("world");

    int width = world.get("width").asInt();
    int height = world.get("height").asInt();

    LOG.info("Creating world with size = [{}, {}]", width, height);

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        WorldTile worldTile = new WorldTile(this.assetService.getTexture("medievalTile_57.png"));
        worldTile.setPosition(i * 64, j * 64);
        worldTile.setSize(64, 64);
        this.worldTiles.add(worldTile);
      }
    }

    LOG.info("Created [{}] tiles", this.worldTiles.size());
  }

  private void loadResources(JsonNode jsonNode) {
    ArrayNode resourcesNode = jsonNode.withArray("resources");

    for (JsonNode node : resourcesNode) {

      int x = node.get("x").asInt();
      int y = node.get("y").asInt();

      ResourceEnum resourceEnum = ResourceEnum.valueOf(node.get("resource").asText());

      int resources = node.get("resources").asInt();
      int maxResources = node.get("maxResources").asInt(resources);

      ResourceStorage resourceStorage = new TotalCapacityResourceStorage(maxResources);
      resourceStorage.addResource(resourceEnum, resources);

      String texture = node.get("texture").asText();

      Resource resource =
          new Resource(this.assetService.getTexture(texture), resourceEnum, resourceStorage);

      resource.setPosition(x * 64, y * 64);
      resource.setSize(64, 64);

      this.resources.add(resource);
    }

    LOG.info("Loaded [{}] resources", this.resources.size());
  }

  private void loadWorkers(JsonNode jsonNode) {
    ArrayNode workersNode = jsonNode.withArray("workers");

    for (JsonNode node : workersNode) {

      int x = node.get("x").asInt();
      int y = node.get("y").asInt();

      int capacity = node.get("capacity").asInt();
      String name = node.get("name").asText();
      String texture = node.get("texture").asText();

      int entityId = world.create();

      componentFactory.createImageActorComponent(entityId, texture);
      componentFactory.createPositionComponent(entityId, x * 64, y * 64, 64, 64);
      componentFactory.createResourceStorageComponent(entityId, capacity);
      componentFactory.createNameComponent(entityId, name);

    }

    LOG.info("Loaded [{}] workers", workersNode.size());
  }

  private void loadBuildings(JsonNode jsonNode) {
    ArrayNode buildingsNode = jsonNode.withArray("buildings");

    for (JsonNode node : buildingsNode) {

      int x = node.get("x").asInt();
      int y = node.get("y").asInt();

      String type = node.get("type").asText();

      String texture = node.get("texture").asText();

      int entityId = this.world.create();

      componentFactory.createImageActorComponent(entityId, texture);
      componentFactory.createPositionComponent(entityId, x * 64, y * 64, 64, 64);

      NameComponent nameComponent = componentFactory.createNameComponent(entityId, "Building");

      if ("WAREHOUSE".equals(type)) {

        componentFactory.createWarehouseComponent(entityId);
        componentFactory.createBuildingComponent(entityId);

        int capacity = node.get("capacity").asInt();
        componentFactory.createResourceStorageComponent(entityId, capacity);

        nameComponent.setName("Warehouse");
      }

    }

    LOG.info("Loaded [{}] buildings", buildingsNode.size());
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
