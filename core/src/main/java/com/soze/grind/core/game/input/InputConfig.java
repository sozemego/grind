package com.soze.grind.core.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.soze.grind.core.game.DebugStage;
import com.soze.grind.core.game.GameStage;
import com.soze.grind.core.game.GameUIStage;
import org.springframework.stereotype.Service;

/**
 * Configures at least some aspects of the input.
 *
 * Adds CameraMouseListener, DebugStage and GameStage to the input multiplexer.
 *
 * These are not all of input listeners, they also exist on UI elements or Actors.
 */
@Service
public class InputConfig {

	private final InputMultiplexer inputMultiplexer = new InputMultiplexer();

	public InputConfig(GameStage gameStage, GameUIStage gameUIStage, DebugStage debugStage, CameraController cameraMouseListener) {

		inputMultiplexer.addProcessor(debugStage);
		inputMultiplexer.addProcessor(gameUIStage);
		inputMultiplexer.addProcessor(cameraMouseListener);
		inputMultiplexer.addProcessor(gameStage);

		Gdx.input.setInputProcessor(inputMultiplexer);
	}
}
