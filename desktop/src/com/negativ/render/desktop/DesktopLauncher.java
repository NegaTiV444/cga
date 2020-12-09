package com.negativ.render.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.negativ.render.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Render4000 Ultimate";
		config.height = 1080;
		config.width = 1920;
		config.fullscreen = true;
		config.foregroundFPS = 90;
		new LwjglApplication(new Main(), config);
	}
}
