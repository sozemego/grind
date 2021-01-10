package com.soze.grind.core.game.world;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.soze.grind.core.game.service.SelectedDungeonContainer;
import com.soze.grind.core.game.service.SelectedObjectContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorldClickListener extends ClickListener {

  private static final Logger LOG = LogManager.getLogger(WorldClickListener.class);

  private final MyWorld myWorld;
  private final SelectedObjectContainer selectedObjectContainer;
  private final SelectedDungeonContainer selectedDungeonContainer;

  @Autowired
  public WorldClickListener(MyWorld myWorld,
      SelectedObjectContainer selectedObjectContainer,
      SelectedDungeonContainer selectedDungeonContainer
  ) {
    this.myWorld = myWorld;
    this.myWorld.addListener(this);

    this.selectedDungeonContainer = selectedDungeonContainer;
    this.selectedObjectContainer = selectedObjectContainer;
  }

  @Override
  public void clicked(InputEvent event, float x, float y) {
    super.clicked(event, x, y);

    selectedDungeonContainer.setSelectedDungeon(null);

    if (event.getTarget() instanceof WorldTile) {
      selectedObjectContainer.setSelectedObject(null);
    } else {
      selectedObjectContainer.setSelectedObject(event.getTarget());
    }

  }

}
