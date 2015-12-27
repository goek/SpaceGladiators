package com.skyking.spacegladiator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class MyGdxGame extends ApplicationAdapter {
    float time;
    public Dest dest;
    private DestInput destInput;

	@Override
	public void create () {
        dest = new Dest();
        destInput = new DestInput(dest);
        Gdx.input.setInputProcessor(destInput);
        Gdx.graphics.setDisplayMode(400, 550, false);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		GlobalTools.batch.begin();
        dest.draw(GlobalTools.batch);
        GlobalTools.batch.end();




	}

    @Override
    public void dispose() {
        super.dispose();
        Assets.dispose();

    }
}
