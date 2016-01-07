package com.skyking.spacegladiator.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.skyking.spacegladiator.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width=1920;
        config.height=1080;
        config.fullscreen = false;

        new LwjglApplication(new MyGdxGame(), config);
	}
}
