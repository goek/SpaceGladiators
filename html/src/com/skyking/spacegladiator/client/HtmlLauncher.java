package com.skyking.spacegladiator.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.skyking.spacegladiator.MyGdxGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1024, 576);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new MyGdxGame();
        }
}