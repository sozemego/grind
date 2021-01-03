package com.soze.grind.core.game.ecs.system;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import org.springframework.stereotype.Service;

@Service
@All(WorkerAiComponent.class)
public class WorkerAiSystem extends BaseEntitySystem {

  private ComponentMapper<WorkerAiComponent> workerAiMapper;

  @Override
  protected void processSystem() {

    IntBag activeEntities = getSubscription().getEntities();
    int[] ids = activeEntities.getData();

    for (int i = 0; i < activeEntities.size(); i++) {

      int id = ids[i];

      WorkerAiComponent workerAiComponent = workerAiMapper.get(id);

      workerAiComponent.getWorkerAI().act(world.getDelta(), world);
    }
  }
}
