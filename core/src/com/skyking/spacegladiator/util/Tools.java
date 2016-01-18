package com.skyking.spacegladiator.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Gök on 27.12.2015.
 */
public class Tools {
    public static final SpriteBatch batch = new SpriteBatch();

    public static void dispose(){
        batch.dispose();
    }

}
