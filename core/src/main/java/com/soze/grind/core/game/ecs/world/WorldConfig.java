package com.soze.grind.core.game.ecs.world;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.soze.grind.core.game.ecs.system.ActorPositionSystem;
import com.soze.grind.core.game.ecs.system.WorkerAiSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorldConfig {

  @Bean
  public World world(ActorPositionSystem actorPositionSystem, WorkerAiSystem workerAiSystem) {

    WorldConfiguration worldConfiguration =
        new WorldConfigurationBuilder().with(actorPositionSystem).with(workerAiSystem).build();

    World world = new World(worldConfiguration);

    return world;
  }
}
