package com.soze.grind.core.game.ecs.world;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.soze.grind.core.game.ecs.system.ActorPositionSystem;
import com.soze.grind.core.game.ecs.system.WorkerAiSystem;
import com.soze.grind.core.game.ecs.system.WorkerProgressBarSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorldConfig {

  @Bean
  public World world(
      ActorPositionSystem actorPositionSystem,
      WorkerAiSystem workerAiSystem,
      WorkerProgressBarSystem workerProgressBarSystem
  ) {

    WorldConfiguration worldConfiguration =
        new WorldConfigurationBuilder()
            .with(actorPositionSystem)
            .with(workerAiSystem)
            .with(workerProgressBarSystem)
            .build();

    World world = new World(worldConfiguration);

    return world;
  }
}
