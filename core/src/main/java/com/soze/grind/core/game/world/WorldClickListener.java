package com.soze.grind.core.game.world;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.soze.grind.core.game.SelectedObjectContainer;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorldClickListener extends ClickListener {

  private final World world;
  private final SelectedObjectContainer selectedObjectContainer;

  @Autowired
  public WorldClickListener(World world, SelectedObjectContainer selectedObjectContainer) {
    this.world = world;
    this.world.addListener(this);
    this.selectedObjectContainer = selectedObjectContainer;
  }

  @Override
  public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
    return super.touchDown(event, x, y, pointer, button);
  }

  @Override
  public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
    super.touchUp(event, x, y, pointer, button);

    if (event.getTarget() instanceof WorldTile) {
      selectedObjectContainer.setSelectedObject(null);
    } else {
      selectedObjectContainer.setSelectedObject(event.getTarget());
    }
  }
}
