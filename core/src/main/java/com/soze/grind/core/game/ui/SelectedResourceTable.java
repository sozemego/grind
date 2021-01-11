package com.soze.grind.core.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.ResourceComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.domain.Resource;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

public class SelectedResourceTable extends Table {

  private final UIElementFactory uiElementFactory;

  private final Resource resource;

  private final Label selectedObjectNameLabel;

  private final Label resourceAmountLabel;

  public SelectedResourceTable(UIElementFactory uiElementFactory, Resource resource) {
    this.uiElementFactory = uiElementFactory;
    this.resource = resource;

    Table headerTable = new Table();

    Table leftTable = new Table();
    Table middleTable = new Table();
    Table rightTable = new Table();

    headerTable.add(leftTable).expandX().right();
    headerTable.add(middleTable);
    headerTable.add(rightTable).expandX();

    ResourceEnum resourceEnum = resource.getResourceEnum();

    Image resourceIcon = uiElementFactory.createImage(resourceEnum.getTextureName());
    leftTable.add(resourceIcon).width(32f).height(32f).right();

    selectedObjectNameLabel = uiElementFactory.createHeaderLabel();
    selectedObjectNameLabel.setText(resource.getName());

    middleTable.add(selectedObjectNameLabel).expandX();

    add(headerTable).width(Value.percentWidth(1f, this)).row();

    add(uiElementFactory.createDivider()).row();

    resourceAmountLabel = uiElementFactory.createTextLabel();

    add(resourceAmountLabel).row();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    ResourceStorage resourceStorage = resource.getResourceStorage();
    ResourceEnum resourceEnum = resource.getResourceEnum();

    int amount = resourceStorage.count(resourceEnum);
    int maxAmount = resourceStorage.maxCapacity(resourceEnum);

    String resourceAmount = amount + "/" + maxAmount;
    resourceAmountLabel.setText("Amount: " + resourceAmount);
  }
}
