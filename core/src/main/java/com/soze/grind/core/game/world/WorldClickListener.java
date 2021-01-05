package com.soze.grind.core.game.world;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.soze.grind.core.game.SelectedObjectContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorldClickListener extends ClickListener {

  private static final Logger LOG = LogManager.getLogger(WorldClickListener.class);

  private final MyWorld myWorld;
  private final SelectedObjectContainer selectedObjectContainer;

  @Autowired
  public WorldClickListener(MyWorld myWorld, SelectedObjectContainer selectedObjectContainer) {
    this.myWorld = myWorld;
    this.myWorld.addListener(this);
    this.selectedObjectContainer = selectedObjectContainer;
  }

  @Override
  public void clicked(InputEvent event, float x, float y) {
    super.clicked(event, x, y);

    if (event.getTarget() instanceof WorldTile) {
      selectedObjectContainer.setSelectedObject(null);
    } else {
      selectedObjectContainer.setSelectedObject(event.getTarget());
    }

  }

}
