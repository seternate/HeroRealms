package com.seternate.herorealms.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.seternate.herorealms.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "HeroRealms";
		//config.height = 768;
		//config.width = 1366;
		//config.fullscreen = true;
		new LwjglApplication(new Main(), config);
	}
}
