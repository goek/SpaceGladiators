package com.skyking.spacegladiator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

    TextureAtlas destRunAtlas;
    Animation destRunAnimation;

    float time;
	@Override
	public void create () {
		batch = new SpriteBatch();

        destRunAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_run.atlas"));
        destRunAnimation = new Animation(1/50f , destRunAtlas.getRegions());
        time = 0;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += Gdx.graphics.getDeltaTime();

		batch.begin();
		//batch.draw(img, 0, 0);
        batch.draw(destRunAnimation.getKeyFrame(time, true), 0, 0);
        batch.end();




	}

    @Override
    public void dispose() {
        super.dispose();
        destRunAtlas.dispose();

    }
}
