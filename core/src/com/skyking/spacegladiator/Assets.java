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
    public static final TextureAtlas destPunchAtlas = new TextureAtlas(Gdx.files.internal("img/anim/dest/dest_punch.atlas"));
    public static final Texture destHealthBar = new Texture(Gdx.files.internal("img/heroHealthBar.png"));
    public static final Texture destHealth = new Texture(Gdx.files.internal("img/heroHealth.png"));

    // Enemy
    public static final TextureAtlas enemyRunAtlas = new TextureAtlas(Gdx.files.internal("img/anim/enemy/dest_run.atlas"));
    public static final TextureAtlas enemyIdleAtlas = new TextureAtlas(Gdx.files.internal("img/anim/enemy/dest_idle.atlas"));
    public static final TextureAtlas enemyPunchAtlas = new TextureAtlas(Gdx.files.internal("img/anim/enemy/dest_punch.atlas"));
    public static final TextureAtlas enemyDieAtlas = new TextureAtlas(Gdx.files.internal("img/anim/enemy/enemy_die.atlas"));
    public static final Texture healthPointTexture = new Texture(Gdx.files.internal("img/healthPoint.png"));

    // Rovenade
    public static final TextureAtlas rovenadeDriveAtlas= new TextureAtlas(Gdx.files.internal("img/anim/rovenade/rovenade_drive.pack"));
    public static final TextureAtlas rovenadeSpawnAtlas= new TextureAtlas(Gdx.files.internal("img/anim/rovenade/rovenade_spawn.pack"));
    public static final TextureAtlas rovenadeOnContactAtlas= new TextureAtlas(Gdx.files.internal("img/anim/rovenade/rovenade_onContact.pack"));


    // Parallax Test
    public static final Texture layer1 = new Texture(Gdx.files.internal("img/parallax/test/layer1.png"));
    public static final Texture layer2 = new Texture(Gdx.files.internal("img/parallax/test/layer2.png"));
    public static final Texture layer3 = new Texture(Gdx.files.internal("img/parallax/test/layer3.png"));
    public static final Texture layer04_middle = new Texture(Gdx.files.internal("img/parallax/layer04_middle.png"));
    public static final Texture map = new Texture(Gdx.files.internal("img/parallax/map.png"));
    public static final Texture world = new Texture(Gdx.files.internal("img/parallax/world.png"));

    public static final Texture introTexture = new Texture(Gdx.files.internal("img/introScreen.png"));
    public static void dispose() {
        destDispose();
        layer1.dispose();
        layer2.dispose();
        layer3.dispose();
        layer04_middle.dispose();
        map.dispose();
        world.dispose();

        healthPointTexture.dispose();

        rovenadeDriveAtlas.dispose();
        rovenadeOnContactAtlas.dispose();
        rovenadeSpawnAtlas.dispose();

        introTexture.dispose();
    }


    private static void destDispose(){
        destRunAtlas.dispose();
        destIdleAtlas.dispose();
        destPunchAtlas.dispose();
        destHealth.dispose();
        destHealthBar.dispose();

        enemyIdleAtlas.dispose();
        enemyPunchAtlas.dispose();
        enemyRunAtlas.dispose();
        enemyDieAtlas.dispose();

    }
}
