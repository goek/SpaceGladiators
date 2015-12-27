package com.skyking.spacegladiator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Gök on 27.12.2015.
 */
public class DestInput extends InputAdapter {
    private Dest dest;

    public DestInput(Dest dest){
        this.dest = dest;
    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.LEFT) {
            switch(dest.getState()) {

                case IDLE:
                    dest.resetTime();
                    dest.setState(Dest.State.IDLETORUN);
                    break;
                case RUNNING:
                    dest.setState(Dest.State.RUNNING);
                    break;
                case ATTACK:
                    break;
                case IDLETORUN:
                    break;
            }


        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT) {
            switch (dest.getState()) {
                case IDLE:
                    dest.setState(Dest.State.IDLE);
                    break;
                case RUNNING:
                    dest.setState(Dest.State.FINISHRUN);
                    break;
                case ATTACK:
                    break;
                case IDLETORUN:
                    dest.setState(Dest.State.RUNTOIDLE);
                    break;
                case RUNTOIDLE:

                    break;
            }
        }

        return super.keyUp(keycode);
    }

}

