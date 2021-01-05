package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerState;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

/** Contains UI for the selected Worker. */
public class SelectedWorkerTable extends Table {

  private final UIElementFactory uiElementFactory;

  private final Entity entity;

  private final Label selectedObjectNameLabel;

  private final ProgressBar progressBar;

  private final Label workerStateLabel;

  public SelectedWorkerTable(UIElementFactory uiElementFactory, Entity entity) {
    this.uiElementFactory = uiElementFactory;
    this.entity = entity;

    selectedObjectNameLabel = uiElementFactory.createHeaderLabel();

    NameComponent nameComponent = entity.getComponent(NameComponent.class);

    selectedObjectNameLabel.setText(nameComponent.getName());

    add(selectedObjectNameLabel).row();

    add(uiElementFactory.createDivider()).row();

    ResourceStorageComponent resourceStorageComponent = entity.getComponent(ResourceStorageComponent.class);

    add(uiElementFactory.createResourceStorageTable(resourceStorageComponent.getResourceStorage())).row();

    workerStateLabel = uiElementFactory.createTextLabel();

    add(workerStateLabel).row();

    progressBar = uiElementFactory.createUIProgressBar();

    add(progressBar)
        .width(Value.percentWidth(0.8f, this))
        .height(16f)
        .row();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    WorkerAiComponent workerAiComponent = entity.getComponent(WorkerAiComponent.class);
    WorkerState workerState = workerAiComponent.getState();

    workerStateLabel.setText(workerState.name());

    progressBar.setVisible(workerState == WorkerState.WORKING);

    progressBar.setProgress(workerAiComponent.getWorkingProgress());
  }
}
