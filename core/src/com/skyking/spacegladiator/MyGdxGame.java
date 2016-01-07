package com.skyking.spacegladiator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.skyking.spacegladiator.screens.ArenaScreen;

public class MyGdxGame extends Game {
    private ArenaScreen arena;

	@Override
	public void create () {
        arena = new ArenaScreen();
        setScreen(arena);
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
        arena.dispose();

    }
}
