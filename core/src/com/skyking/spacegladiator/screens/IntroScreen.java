package com.skyking.spacegladiator.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.util.Tools;

import javax.tools.Tool;

import javafx.stage.Screen;

/**
 * Created by Gök on 18.01.2016.
 */
public class IntroScreen implements com.badlogic.gdx.Screen{
    Game myGame;
    Sprite introSprite = new Sprite(Assets.introTexture);
    ArenaScreen arena;

    public IntroScreen(Game myGame){
        introSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.myGame = myGame;
    }

    @Override
    public void show() {
        arena = new ArenaScreen();
    }

    @Override
    public void render(float delta) {
        Tools.batch.begin();
        introSprite.draw(Tools.batch);
        Tools.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY )) myGame.setScreen(arena);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    arena.dispose();
    }


}
