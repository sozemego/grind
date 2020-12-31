package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.Texture;
import com.google.common.eventbus.EventBus;
import com.soze.grind.core.game.assets.AssetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.soze.grind.core.game")
public class SpringConfig {

  @Bean
  public SelectedObjectMarker selectedObjectMarker(
      SelectedObjectContainer selectedObjectContainer,
      AssetService assetService
  ) {
    Texture texture = assetService.getTexture("panel_blue_empty.png");
    return new SelectedObjectMarker(texture, selectedObjectContainer);
  }

  @Bean
  public EventBus eventBus() {
    return new EventBus();
  }
}
