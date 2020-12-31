package com.soze.grind.core.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.soze.grind.core.game.building.Building;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

/** Contains the UI for selected building. */
public class SelectedBuildingTable extends Table {

  private final UIElementFactory uiElementFactory;

  private final Building building;

  private final Label selectedObjectNameLabel;

  public SelectedBuildingTable(UIElementFactory uiElementFactory, Building building) {
    this.uiElementFactory = uiElementFactory;
    this.building = building;

    this.selectedObjectNameLabel = this.uiElementFactory.createHeaderLabel();
    this.selectedObjectNameLabel.setText(this.building.getName());

    this.add(this.selectedObjectNameLabel).row();

    this.add(this.uiElementFactory.createDivider()).row();

    building
        .getResourceStorage()
        .ifPresent(storage -> this.add(this.uiElementFactory.createResourceStorageTable(storage)).row());
  }
}
