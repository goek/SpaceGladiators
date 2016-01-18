package com.skyking.spacegladiator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.skyking.spacegladiator.screens.ArenaScreen;
import com.skyking.spacegladiator.screens.IntroScreen;
import com.skyking.spacegladiator.util.Tools;

public class MyGdxGame extends Game {
    private IntroScreen intro;

	@Override
	public void create () {
        Gdx.graphics.setVSync(true);
        for(Texture texture : Assets.destIdleAtlas.getTextures()){

            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        intro = new IntroScreen(this);
        setScreen(intro);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.4f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
	}

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();
        intro.dispose();
        Tools.dispose();

    }
}
