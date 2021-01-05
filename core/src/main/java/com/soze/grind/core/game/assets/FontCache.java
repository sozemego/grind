package com.soze.grind.core.game.assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Stores fonts at various sizes.
 */
@Service
public class FontCache {

	private final Map<String, BitmapFont> fonts = new HashMap<>();

	/**
	 * Adds a given font to the cache.
	 *
	 * @param font font to add
	 */
	public void addFont(BitmapFont font) {
		String name = font.getData().name;

		fonts.put(name, font);
	}

	/**
	 * Attempts to retrieve a font with given name.
	 *
	 * @param name name of the font
	 * @return
	 */
	public Optional<BitmapFont> getFont(String name) {
		return Optional.ofNullable(fonts.get(name));
	}

}
