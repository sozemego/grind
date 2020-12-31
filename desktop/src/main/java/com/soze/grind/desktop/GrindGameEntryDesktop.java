package com.soze.grind.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.soze.grind.core.GrindGameEntry;

public class GrindGameEntryDesktop {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 2400;
		config.height = 1080;

		new LwjglApplication(new GrindGameEntry(), config);
	}
}
