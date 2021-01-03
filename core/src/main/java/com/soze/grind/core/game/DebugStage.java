package com.soze.grind.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import com.soze.grind.core.game.world.MyWorld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugStage extends Stage {

  private final MyWorld myWorld;

  private final Label debugLabel;

  private final Viewport gameStageViewport;

  private final GameSpeedService gameSpeedService;

  @Autowired
  public DebugStage(
      Viewport gameStageViewport,
      MyWorld myWorld,
      UIElementFactory uiElementFactory,
      GameSpeedService gameSpeedService
  ) {
    this.gameStageViewport = gameStageViewport;
    this.myWorld = myWorld;
    this.gameSpeedService = gameSpeedService;

    OrthographicCamera camera = new OrthographicCamera();
    ScreenViewport viewport = new ScreenViewport(camera);

    setViewport(viewport);

    debugLabel = uiElementFactory.createTextLabel(Color.WHITE);
    addActor(debugLabel);
  }

  @Override
  public void draw() {
    super.draw();

    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();

    debugLabel.setText(assembleDebugText());
    debugLabel.setPosition(0, height - 100);
  }

  public void resize(int width, int height) {
    getViewport().update(width, height, true);
  }

  private String assembleDebugText() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("FPS: %s \n", Gdx.graphics.getFramesPerSecond()));
    sb.append(String.format("Frame time: %.4f \n", Gdx.graphics.getDeltaTime()));

    Camera camera = this.gameStageViewport.getCamera();

    sb.append(String.format("Game stage x, y: %s %s \n", camera.position.x, camera.position.y));
//    sb.append(String.format("Buildings: %s \n", myWorld.getBuildingsLayer().getBuildings().size()));

    sb.append(String.format("Paused: %s \n", this.gameSpeedService.isPaused()));
    sb.append(String.format("Game speed: %s \n", this.gameSpeedService.getGameSpeed()));

    return sb.toString();
  }

  @Override
  public boolean keyUp(int keyCode) {
    if (keyCode == Keys.L) {
      this.myWorld.animateEnterWorld(this.gameStageViewport.getCamera());
    }

    return super.keyUp(keyCode);
  }
}
