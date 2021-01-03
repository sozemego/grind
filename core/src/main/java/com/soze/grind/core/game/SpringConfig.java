package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.eventbus.EventBus;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.loader.LevelLoader;
import com.soze.grind.core.game.resource.ResourceLayer;
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
  public TileLayer tileLayer(LevelLoader loader) {
    return new TileLayer(loader.getWorldTiles());
  }

  @Bean
  public ResourceLayer resourceLayer(LevelLoader loader) {
    return new ResourceLayer(loader.getResources());
  }

  @Bean
  public Camera camera() {
    return new OrthographicCamera();
  }

  @Bean
  public Viewport viewport(Camera camera) {
    return new ScreenViewport(camera);
  }

  @Bean
  public EventBus eventBus() {
    return new EventBus();
  }
}
