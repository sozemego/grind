package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

/** Contains the UI for selected building. */
public class SelectedBuildingTable extends Table {

  private final UIElementFactory uiElementFactory;

  private final Entity entity;

  private final Label selectedObjectNameLabel;

  public SelectedBuildingTable(UIElementFactory uiElementFactory, Entity entity) {
    this.uiElementFactory = uiElementFactory;
    this.entity = entity;

    NameComponent nameComponent = entity.getComponent(NameComponent.class);

    selectedObjectNameLabel = uiElementFactory.createHeaderLabel();
    selectedObjectNameLabel.setText(nameComponent.getName());

    add(selectedObjectNameLabel).row();

    add(uiElementFactory.createDivider()).row();

    ResourceStorageComponent resourceStorageComponent = entity.getComponent(ResourceStorageComponent.class);

    if (resourceStorageComponent != null) {
      add(uiElementFactory.createResourceStorageTable(resourceStorageComponent.getResourceStorage())).row();
    }
  }
}
