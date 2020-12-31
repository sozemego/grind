package com.soze.grind.core.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.soze.grind.core.game.resource.Resource;
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

    Image resourceIcon = uiElementFactory.createImage(this.resource.getResourceEnum().getTextureName());
    leftTable.add(resourceIcon).width(32f).height(32f).right();

    this.selectedObjectNameLabel = this.uiElementFactory.createHeaderLabel();
    this.selectedObjectNameLabel.setText(this.resource.getResourceEnum().getName());

    middleTable.add(this.selectedObjectNameLabel).expandX();

    this.add(headerTable).width(Value.percentWidth(1f, this)).row();

    this.add(this.uiElementFactory.createDivider()).row();

    this.resourceAmountLabel = this.uiElementFactory.createTextLabel();

    this.add(this.resourceAmountLabel).row();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    int amount = this.resource.getAmountResource();
    int maxAmount = this.resource.getCapacity();

    String resourceAmount = amount + "/" + maxAmount;
    this.resourceAmountLabel.setText("Amount: " + resourceAmount);
  }
}
