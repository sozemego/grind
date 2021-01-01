package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.Texture;
import com.google.common.eventbus.EventBus;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.loader.LevelLoader;
import com.soze.grind.core.game.resource.ResourceLayer;
import com.soze.grind.core.game.unit.WorkerLayer;
import com.soze.grind.core.game.world.BuildingsLayer;
import com.soze.grind.core.game.world.TileLayer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.soze.grind.core.game")
public class SpringConfig {

  @Bean
  public SelectedObjectMarker selectedObjectMarker(
      SelectedObjectContainer selectedObjectContainer, AssetService assetService) {
    Texture texture = assetService.getTexture("panel_blue_empty.png");
    return new SelectedObjectMarker(texture, selectedObjectContainer);
  }

  @Bean
  public BuildingsLayer buildingsLayer(LevelLoader loader) {
    return new BuildingsLayer(loader.getBuildings());
  }

  @Bean
  public WorkerLayer workerLayer(LevelLoader loader) {
    return new WorkerLayer(loader.getWorkers());
  }

  @Bean
  public TileLayer tileLayer(LevelLoader loader) {
    return new TileLayer(loader.getWorldTiles());
  }

  @Bean
  public ResourceLayer resourceLayer(LevelLoader loader) {
    return new ResourceLayer(loader.getResources());
  }

  @Bean
  public EventBus eventBus() {
    return new EventBus();
  }
}
