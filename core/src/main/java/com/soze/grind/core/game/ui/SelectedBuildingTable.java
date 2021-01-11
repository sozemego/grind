package com.soze.grind.core.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.soze.grind.core.game.ecs.domain.Building;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import java.util.Objects;

/** Contains the UI for selected building. */
public class SelectedBuildingTable extends Table {

  private final UIElementFactory uiElementFactory;

  private final Building building;

  private final Label selectedObjectNameLabel;

  public SelectedBuildingTable(UIElementFactory uiElementFactory, Building building) {
    this.uiElementFactory = uiElementFactory;
    this.building = building;

    selectedObjectNameLabel = uiElementFactory.createHeaderLabel();
    selectedObjectNameLabel.setText(building.getName());

    add(selectedObjectNameLabel).row();

    add(uiElementFactory.createDivider()).row();

    ResourceStorage resourceStorage = building.getResourceStorage();

    if (Objects.nonNull(resourceStorage)) {
      add(uiElementFactory.createResourceStorageTable(resourceStorage)).row();
    }
  }
}
