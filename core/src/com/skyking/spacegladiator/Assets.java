package com.skyking.spacegladiator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Gök on 27.12.2015.
 */
public class Assets {
    public AssetManager assetManager;

    public Assets(){
        assetManager = new AssetManager();
    }
    // Dest
    public static final TextureAtlas destRunAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_run.atlas"));
    public static final TextureAtlas destIdleAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_idle.atlas"));
    public static final TextureAtlas destIdleToRunAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_idleToRun.atlas"));
    public static final TextureAtlas destRunToIdleAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_runToIdle.atlas"));
    public static final TextureAtlas destPunchAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_punch.atlas"));

    // Parallax Test
    public static final Texture layer1 = new Texture(Gdx.files.internal("img/parallax/test/layer1.png"));
    public static final Texture layer2 = new Texture(Gdx.files.internal("img/parallax/test/layer2.png"));
    public static final Texture layer3 = new Texture(Gdx.files.internal("img/parallax/test/layer3.png"));
    public static final Texture layer04_middle = new Texture(Gdx.files.internal("img/parallax/layer04_middle.png"));
    public static final Texture map = new Texture(Gdx.files.internal("img/parallax/map.png"));
    public static final Texture world = new Texture(Gdx.files.internal("img/parallax/world.png"));

    public static void dispose() {
        destDispose();
        layer1.dispose();
        layer2.dispose();
        layer3.dispose();
        layer04_middle.dispose();
        map.dispose();
        world.dispose();
    }


    private static void destDispose(){
        destRunAtlas.dispose();
        destIdleAtlas.dispose();
        destIdleToRunAtlas.dispose();
        destRunToIdleAtlas.dispose();
        destPunchAtlas.dispose();



    }
}
