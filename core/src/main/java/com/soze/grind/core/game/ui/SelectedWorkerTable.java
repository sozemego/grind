package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerState;
import com.soze.grind.core.game.ecs.domain.Worker;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

/** Contains UI for the selected Worker. */
public class SelectedWorkerTable extends Table {

  private final UIElementFactory uiElementFactory;

  private final Worker worker;

  private final Label selectedObjectNameLabel;

  private final ProgressBar progressBar;

  private final Label workerStateLabel;

  public SelectedWorkerTable(UIElementFactory uiElementFactory, Worker worker) {
    this.uiElementFactory = uiElementFactory;
    this.worker = worker;

    selectedObjectNameLabel = uiElementFactory.createHeaderLabel();

    selectedObjectNameLabel.setText(worker.getName());

    add(selectedObjectNameLabel).row();

    add(uiElementFactory.createDivider()).row();

    ResourceStorage resourceStorage = worker.getResourceStorage();

    add(uiElementFactory.createResourceStorageTable(resourceStorage)).row();

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

    WorkerState workerState = worker.getWorkerState();

    workerStateLabel.setText(workerState.name());

    progressBar.setVisible(workerState == WorkerState.WORKING);

    progressBar.setProgress(worker.getWorkingProgress());
  }
}
