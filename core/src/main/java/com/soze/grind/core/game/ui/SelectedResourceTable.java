package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.ResourceComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

public class SelectedResourceTable extends Table {

  private final UIElementFactory uiElementFactory;

  private final Entity entity;

  private final Label selectedObjectNameLabel;

  private final Label resourceAmountLabel;

  public SelectedResourceTable(UIElementFactory uiElementFactory, Entity entity) {
    this.uiElementFactory = uiElementFactory;
    this.entity = entity;

    Table headerTable = new Table();

    Table leftTable = new Table();
    Table middleTable = new Table();
    Table rightTable = new Table();

    headerTable.add(leftTable).expandX().right();
    headerTable.add(middleTable);
    headerTable.add(rightTable).expandX();

    ResourceComponent resourceComponent = entity.getComponent(ResourceComponent.class);
    ResourceEnum resourceEnum = resourceComponent.getResourceEnum();

    Image resourceIcon = uiElementFactory.createImage(resourceEnum.getTextureName());
    leftTable.add(resourceIcon).width(32f).height(32f).right();

    NameComponent nameComponent = entity.getComponent(NameComponent.class);
    this.selectedObjectNameLabel = this.uiElementFactory.createHeaderLabel();
    this.selectedObjectNameLabel.setText(nameComponent.getName());

    middleTable.add(this.selectedObjectNameLabel).expandX();

    this.add(headerTable).width(Value.percentWidth(1f, this)).row();

    this.add(this.uiElementFactory.createDivider()).row();

    this.resourceAmountLabel = this.uiElementFactory.createTextLabel();

    this.add(this.resourceAmountLabel).row();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    ResourceStorage resourceStorage =
        entity.getComponent(ResourceStorageComponent.class).getResourceStorage();
    ResourceEnum resourceEnum = entity.getComponent(ResourceComponent.class).getResourceEnum();

    int amount = resourceStorage.count(resourceEnum);
    int maxAmount = resourceStorage.maxCapacity(resourceEnum);

    String resourceAmount = amount + "/" + maxAmount;
    this.resourceAmountLabel.setText("Amount: " + resourceAmount);
  }
}
