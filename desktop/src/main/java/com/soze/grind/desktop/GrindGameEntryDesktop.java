package com.soze.grind.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.soze.grind.core.GrindGameEntry;

public class GrindGameEntryDesktop {
	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setWindowedMode(1800, 700);
		config.useOpenGL3(true, 3, 2);

		config.setWindowPosition(-1900, 100);


		new Lwjgl3Application(new GrindGameEntry(args), config);
	}
}
