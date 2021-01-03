package com.soze.grind.core.game.unit;

import com.soze.grind.core.game.world.MyWorld;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Responsible for updating the workers. */
@Service
public class WorkersLogicService {

  private final MyWorld myWorld;

  @Autowired
  public WorkersLogicService(MyWorld myWorld) {
    this.myWorld = myWorld;
  }

  public void update(float delta) {

    List<Worker> workers = this.myWorld.getWorkerLayer().getWorkers();

    for (Worker worker : workers) {
      worker.update(delta, this.myWorld);
    }
  }
}
