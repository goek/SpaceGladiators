package com.skyking.spacegladiator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.skyking.spacegladiator.screens.ArenaScreen;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by Gök on 10.01.2016.
 */
public class CamAccessor implements TweenAccessor<OrthographicCamera> {
    public static final int ANGLE = 0;
    public static final int POSITION = 1;
    @Override
    public int getValues(OrthographicCamera target, int tweenType, float[] returnValues) {
        switch (tweenType){
            case ANGLE:
                returnValues[0] = 0;
                return 1;
            case POSITION:
                returnValues[0] = target.position.x;
                returnValues[1] = target.position.y;
                return 2;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(OrthographicCamera target, int tweenType, float[] newValues) {
            switch (tweenType){
                case ANGLE:
                    target.rotate(newValues[0]);
                    break;
                case POSITION:
                    target.position.set(newValues[0], newValues[1], 0);
                    break;
                default:
                    assert false;
            }
    }
}
