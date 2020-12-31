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

	private final Map<String, List<FontDescription>> fonts = new HashMap<>();

	public void addFont(String name, BitmapFont font, int size) {
		FontDescription fontDescription = new FontDescription(name, font, size);

		List<FontDescription> descriptions = this.fonts.getOrDefault(name, new ArrayList<>());

		descriptions.add(fontDescription);

		fonts.put(name, descriptions);
	}

	public Optional<BitmapFont> getFont(String name, int size) {
		List<FontDescription> descriptions = this.fonts.getOrDefault(name, new ArrayList<>());

    for (FontDescription description : descriptions) {
    	if (description.name.equals(name) && description.size == size) {
    		return Optional.of(description.font);
			}
    }

    return Optional.empty();
	}

	public static class FontDescription {
		public final String name;
		public final BitmapFont font;
		public final int size;

		public FontDescription(String name, BitmapFont font, int size) {
			this.name = name;
			this.font = font;
			this.size = size;
		}
	}

}
