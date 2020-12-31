package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.grind.core.game.ui.SelectedObjectTableContainer;
import com.soze.grind.core.game.world.World;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Stage which contains all the UI elements of the game. */
@Service
public class GameUIStage extends Stage {

  private static final Logger LOG = LogManager.getLogger(GameUIStage.class);

  private final World world;
  private final SelectedObjectContainer selectedObjectContainer;

  private final SelectedObjectTableContainer selectedObjectTableContainer;

  @Autowired
  public GameUIStage(
      World world,
      SelectedObjectContainer selectedObjectContainer,
      SelectedObjectTableContainer selectedObjectTableContainer) {
    this.world = world;
    this.selectedObjectContainer = selectedObjectContainer;
    this.selectedObjectTableContainer = selectedObjectTableContainer;

    OrthographicCamera camera = new OrthographicCamera();
    ScreenViewport viewport = new ScreenViewport(camera);
    setViewport(viewport);
  }

  @PostConstruct
  public void setup() {
    LOG.info("Setting up GameUIStage");

    this.setupSelectedObjectTable();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }

  private void setupSelectedObjectTable() {

    Table rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.top().right();

    this.addActor(rootTable);

    rootTable.add(this.selectedObjectTableContainer).width(360f).minHeight(500f);
  }
}
