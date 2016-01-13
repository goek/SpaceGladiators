package com.skyking.spacegladiator.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Adding direct setting of angle for Orthographic Camera
 * Created by Gök on 12.01.2016.
 */
public class MyOrthoCam extends OrthographicCamera {
    private float angle = 0;

    public MyOrthoCam(float viewportWidth, float viewportHeight){
        super(viewportWidth, viewportHeight);
    }
    public void setAngle(float angle) {
        super.rotate(angle - this.angle);
        this.angle = angle;
    }

    @Override
    public void rotate(float angle) {
        super.rotate(angle);
        this.angle += angle;
    }

    public float getAngle() {
        return angle;
    }
}
