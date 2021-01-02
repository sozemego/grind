package com.soze.grind.core.game.input;

import com.badlogic.gdx.input.GestureDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Listeners to input events and controllers the camera (e.g. panning). */
@Service
public class CameraController extends GestureDetector {

  @Autowired
  public CameraController(CameraControllerGestureListener cameraControllerGestureListener) {
    super(cameraControllerGestureListener);
  }
}
