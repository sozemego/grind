package com.soze.grind.core.game.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class CameraControllerGestureListener extends GestureAdapter {

	private static final Logger LOG = LogManager.getLogger(CameraControllerGestureListener.class);

	private final Camera camera;

	public CameraControllerGestureListener(Camera camera) {
		this.camera = camera;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		camera.position.add(-deltaX, deltaY, 0);
		return true;
	}


}
