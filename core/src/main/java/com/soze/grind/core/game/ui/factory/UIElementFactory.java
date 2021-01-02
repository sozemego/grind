package com.soze.grind.core.game.ui.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.ui.ProgressBar;
import com.soze.grind.core.game.ui.ResourceStorageTable;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Contains methods for creating various UI elements. */
@Service
public class UIElementFactory {

  private final AssetService assetService;

  @Autowired
  public UIElementFactory(AssetService assetService) {
    this.assetService = assetService;
  }

  public ResourceStorageTable createResourceStorageTable(ResourceStorage storage) {
    return new ResourceStorageTable(storage, this);
  }

  /** Creates a text label with the default font at a default size with a given color. */
  public Label createTextLabel(Color color) {
    BitmapFont font = this.assetService.getFont("accp-22");

    LabelStyle labelStyle = new LabelStyle();
    labelStyle.fontColor = color;
    labelStyle.font = font;

    return new Label("", labelStyle);
  }

  /** Creates a text label with the default font at a default size, in a default color. */
  public Label createTextLabel() {
    return createTextLabel(Color.BLACK);
  }

  /** Creates a Label to be used as a header in UI elements. */
  public Label createHeaderLabel() {
    BitmapFont font = this.assetService.getFont("accp-24");

    LabelStyle labelStyle = new LabelStyle();
    labelStyle.fontColor = Color.BLACK;
    labelStyle.font = font;

    return new Label("", labelStyle);
  }

  public Image createDivider() {
    return new Image(this.assetService.getTexture("grey_sliderHorizontal.png"));
  }

  /** Creates an Image with given texture. */
  public Image createImage(String textureName) {
    return new Image(this.assetService.getTexture(textureName));
  }

  /**
   * Creates a progress bar for given progress supplier.
   * To be placed in the UI elements.
   */
  public ProgressBar createUIProgressBar(Supplier<Float> progressSupplier) {
    return new ProgressBar(
        this.assetService.getTexture("glassPanel.png"),
        this.assetService.getTexture("black_button00_hollow.png"),
        this.assetService.getTexture("red_button13.png"),
        progressSupplier
		);
  }

  /**
   * Creates a progress bar for given progress supplier.
   * To be placed in the game world.
   */
  public ProgressBar createGameWorldProgressBar(Supplier<Float> progressSupplier) {
    return new ProgressBar(
        this.assetService.getTexture("glassPanel.png"),
        this.assetService.getTexture("black_button00_hollow.png"),
        this.assetService.getTexture("red_button13.png"),
        progressSupplier
    );
  }
}
