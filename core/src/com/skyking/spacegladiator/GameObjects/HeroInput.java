package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Gök on 05.01.2016.
 */
public class HeroInput extends InputAdapter {
    private Hero hero;

    public HeroInput(Hero hero){
        this.hero = hero;
    }


    @Override
    public boolean keyDown(int keycode) {

        switch(keycode){
            case Input.Keys.RIGHT:
                if(hero.getState() == Hero.State.ATTACK || hero.getState() == Hero.State.JUMP) break;
                hero.run(Hero.Orientation.RIGHT);
                break;
            case Input.Keys.LEFT:
                if(hero.getState() == Hero.State.ATTACK || hero.getState() == Hero.State.JUMP) break;
                hero.run(Hero.Orientation.LEFT);
                break;
            case Input.Keys.X:
                hero.punch();
                break;
            case Input.Keys.SPACE:
                //hero.jump();
                break;
            case Input.Keys.BACK:
                Gdx.app.exit();
                break;
        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.RIGHT:
                if(hero.getState() == Hero.State.ATTACK || hero.getState() == Hero.State.JUMP) break;
                hero.stop();
                break;
            case Input.Keys.LEFT:
                if(hero.getState() == Hero.State.ATTACK || hero.getState() == Hero.State.JUMP) break;
                hero.stop();
                break;
        }
        return super.keyUp(keycode);
    }

    // for input polling
    public void update() {
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if (hero.getState() == Hero.State.IDLE && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                hero.run(Hero.Orientation.LEFT);
            }
            if (hero.getState() == Hero.State.IDLE && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                hero.run(Hero.Orientation.RIGHT);
            }
            if (hero.getState() == Hero.State.JUMP && Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                hero.jumpVelocity = -Hero.HeroConstants.JUMPVELOCITY;
            } else if (hero.getState() == Hero.State.JUMP && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                hero.jumpVelocity = Hero.HeroConstants.JUMPVELOCITY;
            } else {
                hero.jumpVelocity = 0;
            }
        }

        if(Gdx.app.getType() == Application.ApplicationType.Android) {
            if(Gdx.input.isTouched()){
                if(Gdx.input.getX() < Gdx.graphics.getWidth()/2){
                    hero.run(Hero.Orientation.LEFT);
                }else {
                    hero.run(Hero.Orientation.RIGHT);
                }
            }
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        hero.stop();
        return super.touchUp(screenX, screenY, pointer, button);
    }


}
