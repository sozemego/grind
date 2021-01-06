package com.soze.grind.core.game;

/**
 * Represents a Dungeon that heroes can go in.
 */
public class Dungeon {

	private final String name;
	private final String texture;

	public Dungeon(String name, String texture) {
		this.name = name;
		this.texture = texture;
	}

	public String getName() {
		return name;
	}

	public String getTexture() {
		return texture;
	}
}
