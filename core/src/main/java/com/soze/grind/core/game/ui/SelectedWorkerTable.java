package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import com.soze.grind.core.game.unit.WorkerAI.WorkerState;

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

    this.selectedObjectNameLabel = this.uiElementFactory.createHeaderLabel();

    NameComponent nameComponent = entity.getComponent(NameComponent.class);

    this.selectedObjectNameLabel.setText(nameComponent.getName());

    this.add(this.selectedObjectNameLabel).row();

    this.add(this.uiElementFactory.createDivider()).row();

    ResourceStorageComponent resourceStorageComponent = entity.getComponent(ResourceStorageComponent.class);

    this.add(this.uiElementFactory.createResourceStorageTable(resourceStorageComponent.getResourceStorage())).row();

    this.workerStateLabel = this.uiElementFactory.createTextLabel();

    this.add(this.workerStateLabel).row();

    this.progressBar = this.uiElementFactory.createUIProgressBar(() -> {
      WorkerAiComponent workerAiComponent = entity.getComponent(WorkerAiComponent.class);
      return workerAiComponent.getWorkerAI().getWorkingProgress();
    });

    this.add(progressBar)
        .width(Value.percentWidth(0.8f, this))
        .height(16f)
        .row();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    WorkerState workerState = entity.getComponent(WorkerAiComponent.class).getWorkerAI().getState();

    this.workerStateLabel.setText(workerState.name());

    this.progressBar.setVisible(workerState == WorkerState.WORKING);
  }
}
