package com.soze.grind.core.game.ecs.world;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.soze.grind.core.game.ecs.system.ActorPositionSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorldConfig {

  @Bean
  public World world(ActorPositionSystem actorPositionSystem) {

    WorldConfiguration worldConfiguration =
        new WorldConfigurationBuilder().with(actorPositionSystem).build();

    World world = new World(worldConfiguration);

    return world;
  }
}
