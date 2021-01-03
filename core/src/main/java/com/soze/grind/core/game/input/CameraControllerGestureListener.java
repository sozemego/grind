package com.soze.grind.core.game.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.soze.grind.core.game.GameStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CameraControllerGestureListener extends GestureAdapter {

	private final Camera camera;
	private final GameStage gameStage;

	@Autowired
	public CameraControllerGestureListener(Camera camera, GameStage gameStage) {
		this.camera = camera;
		this.gameStage = gameStage;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		camera.position.add(-deltaX, deltaY, 0);
		return true;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		/**
		 * This controller is the first input processor, so returning true from this method stops the propagation
		 * of touchUp event to other input processors, and so the touchFocus is never cleared if this is not called.
		 */
		this.gameStage.cancelTouchFocus();

		return true;
	}
}
