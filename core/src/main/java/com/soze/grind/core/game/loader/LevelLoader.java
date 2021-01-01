package com.soze.grind.core.game.loader;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.resource.Resource;
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

	private final AssetService assetService;
	private final String currentLevelName;

	private final List<WorldTile> worldTiles = new ArrayList<>();
	private final List<Actor> buildings = new ArrayList<>();
	private final List<Worker> workers = new ArrayList<>();
	private final List<Resource> resources = new ArrayList<>();

	@Autowired
	public LevelLoader(AssetService assetService, @Value("${currentLevelName}") String currentLevelName) {
		this.assetService = assetService;
		this.currentLevelName = currentLevelName;
	}

	@PostConstruct
	public void setup() {
		this.loadLevel(this.currentLevelName);
	}

	public List<WorldTile> getWorldTiles() {
		return worldTiles;
	}

	public List<Actor> getBuildings() {
		return buildings;
	}

	public List<Worker> getWorkers() {
		return workers;
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

	}

	private void loadWorkers(JsonNode jsonNode) {

	}

	private void loadBuildings(JsonNode jsonNode) {

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
