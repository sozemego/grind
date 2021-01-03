package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.google.common.eventbus.Subscribe;
import com.soze.grind.core.game.SelectedObjectContainer;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.ecs.component.BuildingComponent;
import com.soze.grind.core.game.event.ObjectSelectedEvent;
import com.soze.grind.core.game.resource.Resource;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import com.soze.grind.core.game.unit.Worker;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Table containing the UI for selected object. */
@Service
public class SelectedObjectTableContainer extends Table {

  private final SelectedObjectContainer selectedObjectContainer;
  private final AssetService assetService;
  private final UIElementFactory uiElementFactory;

  private Table currentSelectedUI;
  private Object selectedObject = null;

  @Autowired
  public SelectedObjectTableContainer(
      SelectedObjectContainer selectedObjectContainer,
      AssetService assetService,
      UIElementFactory uiElementFactory
  ) {
    this.selectedObjectContainer = selectedObjectContainer;
    this.assetService = assetService;
    this.uiElementFactory = uiElementFactory;

    this.top();

    NinePatchDrawable ninePatchDrawable =
        new NinePatchDrawable(new NinePatch(this.assetService.getTexture("grey_panel.png"), 8, 8, 8, 8));

    this.setBackground(ninePatchDrawable);
  }

  @Subscribe
  public void onObjectSelected(ObjectSelectedEvent event) {
    this.setCurrentSelectedUI(event.selectedObject);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }

  private void setCurrentSelectedUI(Object nextSelectedObject) {

    // 4 options

    // 1 nothing selected previously, nothing selected now
    if (Objects.isNull(this.selectedObject) && Objects.isNull(nextSelectedObject)) {
      this.clearCurrentTable();
    }

    // 2 something selected previously, nothing selected now
    if (Objects.nonNull(this.selectedObject) && Objects.isNull(nextSelectedObject)) {
      this.clearCurrentTable();
    }

    // 3 nothing selected previously, now something is selected
    if (Objects.isNull(this.selectedObject) && Objects.nonNull(nextSelectedObject)) {
      this.createSelectedTable(nextSelectedObject);
    }

    // 4 something selected previously, now something is selected
    if (Objects.nonNull(this.selectedObject) && Objects.nonNull(nextSelectedObject)) {

      if (this.selectedObject != nextSelectedObject) {
        this.clearCurrentTable();
        this.createSelectedTable(nextSelectedObject);
      }
    }
  }

  private void createSelectedTable(Object nextSelectedObject) {
    this.setVisible(true);

    this.selectedObject = nextSelectedObject;

    if (selectedObject instanceof Entity) {

      Entity entity = (Entity) selectedObject;

      if (Objects.nonNull(entity.getComponent(BuildingComponent.class))) {
        currentSelectedUI = new SelectedBuildingTable(uiElementFactory, entity);
      }

    }

    if (selectedObject instanceof Worker) {
      currentSelectedUI = new SelectedWorkerTable(uiElementFactory, (Worker) selectedObject);
    }

    if (selectedObject instanceof Resource) {
      currentSelectedUI = new SelectedResourceTable(uiElementFactory, (Resource) selectedObject);
    }

    if (Objects.nonNull(currentSelectedUI)) {
      addActor(currentSelectedUI);
      currentSelectedUI.setFillParent(true);
      currentSelectedUI.top();
    }

    this.getColor().a = 0;

    this.addAction(Actions.fadeIn(0.25f));
  }

  /** Clears the current table. */
  private void clearCurrentTable() {
    this.clearActions();
    this.selectedObject = null;

    this.setVisible(false);

    if (Objects.nonNull(currentSelectedUI)) {
      removeActor(currentSelectedUI);
    }
  }
}
