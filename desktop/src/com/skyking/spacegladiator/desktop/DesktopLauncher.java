package com.skyking.spacegladiator.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.skyking.spacegladiator.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        Vector2 res1 = new Vector2(1920, 1080);
        Vector2 res2 = new Vector2(1600, 900);
        Vector2 res3 = new Vector2(1280, 720);
        Vector2 res4 = new Vector2(640, 360);
        config.width = (int) res3.x;
        config.height = (int) res3.y;
        config.fullscreen = false;


        new LwjglApplication(new MyGdxGame(), config);
	}
}
