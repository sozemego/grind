package com.soze.grind.core.game.loader;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.resource.Resource;
import com.soze.grind.core.game.unit.Worker;
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

	public void loadLevel(String levelName) {
		LOG.info("Loading level = " + levelName);
	}



}
