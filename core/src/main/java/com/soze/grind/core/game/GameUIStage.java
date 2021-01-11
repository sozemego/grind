package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.soze.grind.core.game.ui.DungeonManagementWindow;
import com.soze.grind.core.game.ui.DungeonSelectionTable;
import com.soze.grind.core.game.ui.SelectedObjectTableContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Stage which contains all the UI elements of the game. */
@Service
public class GameUIStage extends Stage {

  private static final Logger LOG = LogManager.getLogger(GameUIStage.class);

  private final SelectedObjectTableContainer selectedObjectTableContainer;
  private final DungeonSelectionTable dungeonSelectionTable;
  private final DungeonManagementWindow dungeonManagementWindow;

  @Autowired
  public GameUIStage(
      SelectedObjectTableContainer selectedObjectTableContainer,
      DungeonSelectionTable dungeonSelectionTable,
      DungeonManagementWindow dungeonManagementWindow
  ) {
    this.selectedObjectTableContainer = selectedObjectTableContainer;
    this.dungeonSelectionTable = dungeonSelectionTable;
    this.dungeonManagementWindow = dungeonManagementWindow;

    OrthographicCamera camera = new OrthographicCamera();
    ScreenViewport viewport = new ScreenViewport(camera);
    setViewport(viewport);

    setup();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }

  private void setup() {
    LOG.info("Setting up GameUIStage");

    setupSelectedObjectTable();
    setupDungeonSelectionTable();
    setupDungeonManagementWindow();
  }

  private void setupSelectedObjectTable() {

    Table rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.top().right();

    addActor(rootTable);

    rootTable.add(selectedObjectTableContainer).width(360f).minHeight(500f);

    selectedObjectTableContainer.setTouchable(Touchable.enabled);

    selectedObjectTableContainer.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {

      }
    });
  }

  private void setupDungeonSelectionTable() {

    Table rootTable = new Table();
    rootTable.setFillParent(true);

    rootTable.left().bottom();

    addActor(rootTable);

    rootTable.add(dungeonSelectionTable).minHeight(150f);

    dungeonSelectionTable.setTouchable(Touchable.enabled);

    dungeonSelectionTable.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {

      }
    });

  }

  private void setupDungeonManagementWindow() {

    Table rootTable = new Table();
    rootTable.setFillParent(true);

    addActor(rootTable);

    rootTable.add(dungeonManagementWindow).width(256f).height(256f);

  }

}
