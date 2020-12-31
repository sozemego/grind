package com.soze.grind.core.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import com.soze.grind.core.game.unit.Worker;
import com.soze.grind.core.game.unit.WorkerAI.WorkerState;

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

    this.selectedObjectNameLabel = this.uiElementFactory.createHeaderLabel();
    this.selectedObjectNameLabel.setText(this.worker.getName());

    this.add(this.selectedObjectNameLabel).row();

    this.add(this.uiElementFactory.createDivider()).row();

    this.add(this.uiElementFactory.createResourceStorageTable(this.worker)).row();

    this.workerStateLabel = this.uiElementFactory.createTextLabel();

    this.add(this.workerStateLabel).row();

    this.progressBar = this.uiElementFactory.createUIProgressBar(this.worker.getWorkerAI()::getWorkingProgress);

    this.add(progressBar)
        .width(Value.percentWidth(0.8f, this))
        .height(16f)
        .row();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    WorkerState workerState = this.worker.getState();

    this.workerStateLabel.setText(this.worker.getStateText());

    this.progressBar.setVisible(workerState == WorkerState.WORKING);
  }
}
