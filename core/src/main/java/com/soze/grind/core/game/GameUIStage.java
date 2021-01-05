package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.grind.core.game.ui.SelectedObjectTableContainer;
import com.soze.grind.core.game.world.MyWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Stage which contains all the UI elements of the game. */
@Service
public class GameUIStage extends Stage {

  private static final Logger LOG = LogManager.getLogger(GameUIStage.class);

  private final MyWorld myWorld;
  private final SelectedObjectContainer selectedObjectContainer;

  private final SelectedObjectTableContainer selectedObjectTableContainer;

  @Autowired
  public GameUIStage(
      MyWorld myWorld,
      SelectedObjectContainer selectedObjectContainer,
      SelectedObjectTableContainer selectedObjectTableContainer
  ) {
    this.myWorld = myWorld;
    this.selectedObjectContainer = selectedObjectContainer;
    this.selectedObjectTableContainer = selectedObjectTableContainer;

    OrthographicCamera camera = new OrthographicCamera();
    ScreenViewport viewport = new ScreenViewport(camera);
    setViewport(viewport);

    setup();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }

  private void setupSelectedObjectTable() {

    Table rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.top().right();

    addActor(rootTable);

    rootTable.add(selectedObjectTableContainer).width(360f).minHeight(500f);
  }

  private void setup() {
    LOG.info("Setting up GameUIStage");

    setupSelectedObjectTable();
  }
}
