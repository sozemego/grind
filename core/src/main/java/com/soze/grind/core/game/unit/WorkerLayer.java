package com.soze.grind.core.game.unit;

import com.badlogic.gdx.scenes.scene2d.Group;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerLayer extends Group {

  private final List<Worker> workers = new ArrayList<>();

  @Autowired
  public WorkerLayer(WorkersLoader workersLoader) {
    this.workers.addAll(workersLoader.getWorkers());
    this.workers.forEach(this::addActor);
  }

  public List<Worker> getWorkers() {
    return workers;
  }
}
