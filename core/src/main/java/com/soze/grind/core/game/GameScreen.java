package com.soze.grind.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL30;
import com.soze.grind.core.game.ecs.world.GameWorld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameScreen extends ScreenAdapter {

  private final GameWorld gameWorld;
  private final GameStage gameStage;
  private final GameUIStage gameUIStage;
  private final DebugStage debugStage;

  private final GameSpeedService gameSpeedService;

  @Autowired
  public GameScreen(
      GameWorld gameWorld,
      GameStage gameStage,
      DebugStage debugStage,
      GameUIStage gameUIStage,
      GameSpeedService gameSpeedService
  ) {
    this.gameWorld = gameWorld;
    this.gameStage = gameStage;
    this.debugStage = debugStage;
    this.gameUIStage = gameUIStage;
    this.gameSpeedService = gameSpeedService;
  }

  @Override
  public void show() {
    gameStage.getCamera().position.set(3200, 3200, 0);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glEnable(GL30.GL_BLEND);
    Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);

    for (int i = 0; i < gameSpeedService.getGameSpeed() && !this.gameSpeedService.isPaused(); i++) {

      this.gameWorld.setDelta(delta);
      this.gameWorld.process();
    }

    this.gameStage.act(delta);
    this.gameUIStage.act(delta);
    this.debugStage.act();

    this.gameStage.draw();
    this.gameUIStage.draw();
    this.debugStage.draw();

  }

  @Override
  public void resize(int width, int height) {
    this.gameStage.getViewport().update(width, height, false);
    this.gameUIStage.getViewport().update(width, height, true);
    this.debugStage.resize(width, height);
  }
}
