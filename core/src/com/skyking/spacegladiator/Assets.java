package com.skyking.spacegladiator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Gök on 27.12.2015.
 */
public class Assets {


    // Dest
    public static final TextureAtlas destRunAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_run.atlas"));
    public static final TextureAtlas destIdleAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_idle.atlas"));
    public static final TextureAtlas destIdleToRunAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_idleToRun.atlas"));
    public static final TextureAtlas destRunToIdleAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_runToIdle.atlas"));


    // Parallax Test
    public static final Texture layer1 = new Texture(Gdx.files.internal("img/parallax/test/layer1.png"));
    public static final Texture layer2 = new Texture(Gdx.files.internal("img/parallax/test/layer2.png"));
    public static final Texture layer3 = new Texture(Gdx.files.internal("img/parallax/test/layer3.png"));

    public static void dispose() {
        destDispose();
    }


    private static void destDispose(){
        destRunAtlas.dispose();
        destIdleAtlas.dispose();
        destIdleToRunAtlas.dispose();
        destRunToIdleAtlas.dispose();
    }
}
