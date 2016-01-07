package com.skyking.spacegladiator.GameObjects;

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
                if(hero.getState() == Hero.State.ATTACK) break;
                hero.run(Hero.Orientation.RIGHT);
                break;
            case Input.Keys.LEFT:
                if(hero.getState() == Hero.State.ATTACK) break;
                hero.run(Hero.Orientation.LEFT);
                break;
            case Input.Keys.X:
                hero.punch();
                break;
        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Input.Keys.RIGHT:
                if(hero.getState() == Hero.State.ATTACK) break;
                hero.stop();
                break;
            case Input.Keys.LEFT:
                if(hero.getState() == Hero.State.ATTACK) break;
                hero.stop();
                break;
        }
        return super.keyUp(keycode);
    }

    // for input polling
    public void update(){
        if(hero.getState() == Hero.State.IDLE && Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            hero.run(Hero.Orientation.LEFT);
        }
        if(hero.getState() == Hero.State.IDLE && Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            hero.run(Hero.Orientation.RIGHT);
        }
    }
}
