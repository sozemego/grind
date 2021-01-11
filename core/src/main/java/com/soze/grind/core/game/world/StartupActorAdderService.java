package com.soze.grind.core.game.world;

import com.soze.grind.core.game.GameStage;
import com.soze.grind.core.game.SelectedObjectMarker;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service adds some initial actors after GameStage and those actors were created.
 */
@Service
public class StartupActorAdderService {

	private final GameStage gameStage;
	private final TileLayer tileLayer;
	private final SelectedObjectMarker selectedObjectMarker;

	@Autowired
	public StartupActorAdderService(GameStage gameStage, TileLayer tileLayer,
			SelectedObjectMarker selectedObjectMarker) {
		this.gameStage = gameStage;
		this.tileLayer = tileLayer;
		this.selectedObjectMarker = selectedObjectMarker;
	}

	@PostConstruct
	public void setup() {
		gameStage.addActor(tileLayer);
		gameStage.addActor(selectedObjectMarker);
	}
}
