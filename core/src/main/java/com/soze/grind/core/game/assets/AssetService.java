package com.soze.grind.core.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Manages assets for the game. */
@Service
public class AssetService {

  private static final Logger LOG = LogManager.getLogger(AssetService.class);

  private final AssetManager assetManager = new AssetManager();

  private final FontCache fontCache;

  @Autowired
  public AssetService(FontCache fontCache) {
    this.fontCache = fontCache;
  }

  /** Loads all assets to be used by the asset manager. */
  @PostConstruct
  public void loadAssets() {
    LOG.info("Loading all assets");

    List<String> allAssets = getAllAssetPaths();
    loadAssets(allAssets);

    this.assetManager.finishLoading();
  }

  /**
   * Gets a given texture.
   */
  public Texture getTexture(String name) {
    return assetManager.get(getAssetName(name));
  }

  /**
   * Gets a given font, at a default size.
   *
   * @param name name of the font
   */
  public BitmapFont getFont(String name) {
    return this.getFont(name, 20);
  }

  /**
   * Gets a given front, at a given size.
   *
   * @param name name of the font
   * @param size size of the font
   */
  public BitmapFont getFont(String name, int size) {
    name = getAssetName(name);

    Optional<BitmapFont> optionalFont = this.fontCache.getFont(name, size);

    if (optionalFont.isPresent()) {
      return optionalFont.get();
    }

    FreeTypeFontGenerator generator =
        new FreeTypeFontGenerator(Gdx.files.internal(name));

    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    parameter.size = size;

    BitmapFont font = generator.generateFont(parameter);

    generator.dispose();

    this.fontCache.addFont(name, font, size);

    return this.getFont(name, size);
  }

  /**
   * Gets the asset name from a given name.
   * Adds <code>assets/</code> to the beginning if not given already.
   *
   * @param name name of the asset
   * @return correct name of the asset
   */
  private String getAssetName(String name) {
    if (name.startsWith("assets/")) {
      return name;
    }
    return "assets/" + name;
  }

  /** Gets file paths to every asset in /assets folder */
  private List<String> getAllAssetPaths() {
    try (Stream<Path> paths = Files.walk(Paths.get("assets"))) {

      return paths
          .filter(Files::isRegularFile)
          .map(Path::toString)
          .collect(Collectors.toList());

    } catch (IOException ex) {
      ex.printStackTrace();
      return new ArrayList<>();
    }
  }

  /**
   * Loads all assets from given list of paths.
   *
   * @param paths paths to every asset
   */
  private void loadAssets(List<String> paths) {
    LOG.info("Loading [{}] assets", paths.size());
    paths.forEach(this::loadAsset);
  }

  /**
   * Loads the asset from given path.
   *
   * @param path path to asset
   */
  private void loadAsset(String path) {
    LOG.info("Loading asset = [{}]", path);

    String[] strings = path.split("\\.");

    String extension = strings[1];

    if ("png".equals(extension)) {
      assetManager.load(path, Texture.class);
    }
  }
}
