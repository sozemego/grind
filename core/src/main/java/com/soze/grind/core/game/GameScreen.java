package com.soze.grind.core.game;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameScreen extends ScreenAdapter {

  private final World world;
  private final GameStage gameStage;
  private final GameUIStage gameUIStage;
  private final DebugStage debugStage;

  private final GameLogicService gameLogicService;
  private final GameSpeedService gameSpeedService;

  @Autowired
  public GameScreen(
      World world,
      GameStage gameStage,
      DebugStage debugStage,
      GameUIStage gameUIStage,
      GameLogicService gameLogicService,
      GameSpeedService gameSpeedService
  ) {
    this.world = world;
    this.gameStage = gameStage;
    this.debugStage = debugStage;
    this.gameUIStage = gameUIStage;
    this.gameLogicService = gameLogicService;
    this.gameSpeedService = gameSpeedService;
  }

  @Override
  public void show() {
    this.gameStage.getCamera().position.set(3200, 3200, 0);

    this.gameStage.start();
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    for (int i = 0; i < this.gameSpeedService.getGameSpeed() && !this.gameSpeedService.isPaused(); i++) {

//      this.gameLogicService.update(delta);

      this.world.setDelta(delta);
      this.world.process();
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
