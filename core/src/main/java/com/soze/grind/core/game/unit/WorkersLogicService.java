package com.soze.grind.core.game.unit;

import com.soze.grind.core.game.world.World;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Responsible for updating the workers. */
@Service
public class WorkersLogicService {

  private final World world;

  @Autowired
  public WorkersLogicService(World world) {
    this.world = world;
  }

  public void update(float delta) {

    List<Worker> workers = this.world.getWorkerLayer().getWorkers();

    for (Worker worker : workers) {
      worker.update(delta, this.world);
    }
  }
}
