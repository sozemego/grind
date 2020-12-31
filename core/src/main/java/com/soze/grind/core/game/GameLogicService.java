package com.soze.grind.core.game;

import com.soze.grind.core.game.unit.WorkersLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Aggregates all services which update game logic and calls them in one place. */
@Service
public class GameLogicService {

  private final WorkersLogicService workersLogicService;

  @Autowired
  public GameLogicService(WorkersLogicService workersLogicService) {
    this.workersLogicService = workersLogicService;
  }

  public void update(float delta) {
    workersLogicService.update(delta);
  }
}
