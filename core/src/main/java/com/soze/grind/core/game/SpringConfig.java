package com.soze.grind.core.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.eventbus.EventBus;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.loader.LevelLoader;
import com.soze.grind.core.game.service.SelectedDungeonContainer;
import com.soze.grind.core.game.service.SelectedObjectContainer;
import com.soze.grind.core.game.ui.DungeonManagementWindow;
import com.soze.grind.core.game.ui.DungeonSelectionTable;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
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
  public DungeonSelectionTable dungeonSelectionTable(
      SelectedDungeonContainer selectedDungeonContainer,
      AssetService assetService,
      UIElementFactory uiElementFactory,
      LevelLoader loader
  ) {
    return new DungeonSelectionTable(
        selectedDungeonContainer, assetService, uiElementFactory, loader.getDungeons());
  }

  @Bean
  public DungeonManagementWindow dungeonManagementWindow(
      AssetService assetService,
      UIElementFactory uiElementFactory
  ) {

    BitmapFont titleFont = assetService.getFont("accp-24");
    Color titleFontColor = Color.BLACK;
    NinePatchDrawable background = assetService.getNinePatchDrawable("grey_panel.png");

    return new DungeonManagementWindow(
        "Dungeon", new WindowStyle(titleFont, titleFontColor, background), uiElementFactory
    );
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
