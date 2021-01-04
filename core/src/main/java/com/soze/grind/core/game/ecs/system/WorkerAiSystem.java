package com.soze.grind.core.game.ecs.system;

import com.artemis.Aspect.Builder;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.soze.grind.core.game.GameStage;
import com.soze.grind.core.game.ecs.component.PositionComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerState;
import com.soze.grind.core.game.ecs.component.WorkerProgressBarComponent;
import com.soze.grind.core.game.ui.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@All({WorkerAiComponent.class, WorkerProgressBarComponent.class, PositionComponent.class})
public class WorkerAiSystem extends BaseEntitySystem {

  private ComponentMapper<WorkerAiComponent> workerAiMapper;
  private ComponentMapper<WorkerProgressBarComponent> workerProgressBarMapper;
  private ComponentMapper<PositionComponent> positionMapper;

  private final GameStage gameStage;

  @Autowired
  public WorkerAiSystem(GameStage gameStage) {
    this.gameStage = gameStage;
  }

  @Override
  protected void inserted(int entityId) {

    WorkerProgressBarComponent workerProgressBarComponent = workerProgressBarMapper.get(entityId);

    gameStage.addActor(workerProgressBarComponent.getProgressBar());

  }

  @Override
  protected void removed(int entityId) {
    WorkerProgressBarComponent workerProgressBarComponent = workerProgressBarMapper.get(entityId);

    workerProgressBarComponent.getProgressBar().remove();
  }

  @Override
  protected void processSystem() {

    IntBag activeEntities = getSubscription().getEntities();
    int[] ids = activeEntities.getData();

    for (int i = 0; i < activeEntities.size(); i++) {

      int id = ids[i];

      WorkerAiComponent workerAiComponent = workerAiMapper.get(id);

      workerAiComponent.act(world.getDelta(), world);

      PositionComponent positionComponent = positionMapper.get(id);

      WorkerProgressBarComponent workerProgressBarComponent = workerProgressBarMapper.get(id);
      ProgressBar progressBar = workerProgressBarComponent.getProgressBar();

      progressBar.setVisible(workerAiComponent.getState() == WorkerState.WORKING);
      progressBar.setProgress(workerAiComponent.getWorkingProgress());
      progressBar.setPosition(positionComponent.getX(), positionComponent.getY());

    }
  }
}
