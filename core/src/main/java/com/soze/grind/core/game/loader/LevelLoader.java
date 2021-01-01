package com.soze.grind.core.game.loader;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.building.Building;
import com.soze.grind.core.game.resource.Resource;
import com.soze.grind.core.game.unit.Worker;
import com.soze.grind.core.game.world.WorldTile;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelLoader {

	private static final Logger LOG = LogManager.getLogger(LevelLoader.class);

	private final AssetService assetService;

	private final List<WorldTile> worldTiles = new ArrayList<>();
	private final List<Actor> buildings = new ArrayList<>();
	private final List<Worker> workers = new ArrayList<>();
	private final List<Resource> resources = new ArrayList<>();

	@Autowired
	public LevelLoader(AssetService assetService) {
		this.assetService = assetService;
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
