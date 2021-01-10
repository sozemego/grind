package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.common.eventbus.Subscribe;
import com.soze.grind.core.game.service.SelectedObjectContainer;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.ecs.component.BuildingComponent;
import com.soze.grind.core.game.ecs.component.HeroComponent;
import com.soze.grind.core.game.ecs.component.ResourceComponent;
import com.soze.grind.core.game.ecs.component.WorkerComponent;
import com.soze.grind.core.game.ecs.domain.Hero;
import com.soze.grind.core.game.event.ObjectSelectedEvent;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
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

    top();

    setBackground(assetService.getNinePatchDrawable("grey_panel.png"));
    setVisible(false);
  }

  @Subscribe
  public void onObjectSelected(ObjectSelectedEvent event) {
    setCurrentSelectedUI(event.selectedObject);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
  }

  private void setCurrentSelectedUI(Object nextSelectedObject) {

    // 4 options

    // 1 nothing selected previously, nothing selected now
    if (Objects.isNull(selectedObject) && Objects.isNull(nextSelectedObject)) {
      clearCurrentTable();
    }

    // 2 something selected previously, nothing selected now
    if (Objects.nonNull(selectedObject) && Objects.isNull(nextSelectedObject)) {
      clearCurrentTable();
    }

    // 3 nothing selected previously, now something is selected
    if (Objects.isNull(selectedObject) && Objects.nonNull(nextSelectedObject)) {
      createSelectedTable(nextSelectedObject);
    }

    // 4 something selected previously, now something is selected
    if (Objects.nonNull(selectedObject) && Objects.nonNull(nextSelectedObject)) {

      if (selectedObject != nextSelectedObject) {
        clearCurrentTable();
        createSelectedTable(nextSelectedObject);
      }
    }
  }

  private void createSelectedTable(Object nextSelectedObject) {
    setVisible(true);

    selectedObject = nextSelectedObject;

    if (selectedObject instanceof Entity) {

      Entity entity = (Entity) selectedObject;

      if (Objects.nonNull(entity.getComponent(BuildingComponent.class))) {
        currentSelectedUI = new SelectedBuildingTable(uiElementFactory, entity);
      }

      if (Objects.nonNull(entity.getComponent(WorkerComponent.class))) {
        currentSelectedUI = new SelectedWorkerTable(uiElementFactory, entity);
      }

      if (Objects.nonNull(entity.getComponent(ResourceComponent.class))) {
        currentSelectedUI = new SelectedResourceTable(uiElementFactory, entity);
      }

      if (Objects.nonNull(entity.getComponent(HeroComponent.class))) {
        currentSelectedUI = new SelectedHeroTable(uiElementFactory, new Hero(entity));
      }

    }

    if (Objects.nonNull(currentSelectedUI)) {
      addActor(currentSelectedUI);
      currentSelectedUI.setFillParent(true);
      currentSelectedUI.top();
    }

    getColor().a = 0;

    addAction(Actions.fadeIn(0.25f));
  }

  /** Clears the current table. */
  private void clearCurrentTable() {
    clearActions();
    selectedObject = null;

    setVisible(false);

    if (Objects.nonNull(currentSelectedUI)) {
      removeActor(currentSelectedUI);
    }
  }
}
