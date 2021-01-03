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

    this.selectedObjectNameLabel = this.uiElementFactory.createHeaderLabel();
    this.selectedObjectNameLabel.setText(nameComponent.getName());

    this.add(this.selectedObjectNameLabel).row();

    this.add(this.uiElementFactory.createDivider()).row();

    ResourceStorageComponent resourceStorageComponent = entity.getComponent(ResourceStorageComponent.class);

    if (resourceStorageComponent != null) {
      this.add(this.uiElementFactory.createResourceStorageTable(resourceStorageComponent.getResourceStorage())).row();
    }
  }
}
