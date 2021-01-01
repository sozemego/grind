package com.soze.grind.core.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.grind.core.game.world.World;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameStage extends Stage {

  private final World world;
  private final SelectedObjectContainer selectedObjectContainer;
  private final GameSpeedService gameSpeedService;

  private final Set<Integer> pressedKeys = new HashSet<>();

  @Autowired
  public GameStage(
      World world,
      SelectedObjectContainer selectedObjectContainer,
      GameSpeedService gameSpeedService
  ) {
    this.world = world;
    this.selectedObjectContainer = selectedObjectContainer;
    this.gameSpeedService = gameSpeedService;

    OrthographicCamera camera = new OrthographicCamera();
    ScreenViewport viewport = new ScreenViewport(camera);
    setViewport(viewport);

    this.addActor(world);
  }

  /** Does some functions needed at the start. */
  public void start() {
    this.world.animateEnterWorld(getCamera());
    if (!this.world.getBuildingsLayer().getBuildings().isEmpty()) {
      this.selectedObjectContainer.setSelectedObject(
          this.world.getBuildingsLayer().getBuildings().get(0));
    }
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    moveCamera();
  }

  @Override
  public boolean keyDown(int keyCode) {
    this.pressedKeys.add(keyCode);
    return true;
  }

  @Override
  public boolean keyUp(int keyCode) {
    if (keyCode == Keys.P) {
      this.gameSpeedService.togglePause();
    }

    if (keyCode == Keys.PLUS) {
      this.gameSpeedService.incrementGameSpeed();
    }

    if (keyCode == Keys.MINUS) {
      this.gameSpeedService.decrementGameSpeed();
    }

    this.pressedKeys.remove(keyCode);
    return true;
  }

  private void moveCamera() {
    if (pressedKeys.contains(Keys.A)) {
      this.getCamera().position.x -= 5;
    }

    if (pressedKeys.contains(Keys.D)) {
      this.getCamera().position.x += 5;
    }

    if (pressedKeys.contains(Keys.W)) {
      this.getCamera().position.y += 5;
    }

    if (pressedKeys.contains(Keys.S)) {
      this.getCamera().position.y -= 5;
    }
  }
}
