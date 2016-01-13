package com.skyking.spacegladiator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.CamAccessor;
import com.skyking.spacegladiator.GameObjects.Enemy;
import com.skyking.spacegladiator.GameObjects.EnemyPhysics;
import com.skyking.spacegladiator.GameObjects.Hero;
import com.skyking.spacegladiator.GameObjects.HeroPhysics;
import com.skyking.spacegladiator.util.Constants;
import com.skyking.spacegladiator.util.GlobalTools;
import com.skyking.spacegladiator.util.MyOrthoCam;
import com.sun.java.swing.plaf.motif.MotifBorders;

import java.lang.reflect.Array;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Created by Gök on 02.01.2016.
 */
public class ArenaScreen implements Screen {
    private MyOrthoCam myCam;
    private float time = 0;
    private float timeStretch = 16f;
    private float zoom =(float) 1920 / Gdx.graphics.getWidth()  ;
    private boolean isFollowing = false;

    private float camOffset = 10f;
    private Sprite[] backgroundLayers;
    private Sprite worldSprite;

    private Hero hero;
    private World arenaWorld;
    private BodyDef groundBodyDef, leftWallBodyDef, rightWallBodyDef;
    private ChainShape groundShape, leftWallShape, rightWallShape;
    private Box2DDebugRenderer box2DDebugRenderer;
    private com.badlogic.gdx.utils.Array<Enemy> enemies;
    TweenManager tweenManager;

    private ArenaLogic arenaLogic;

    private static final float SCALE = 20f ;

    public ArenaScreen(){
        initArenaScreen();
        time = 0;

        Gdx.input.setCatchBackKey(false);
    }

    private void initArenaScreen() {
        arenaWorld = new World(new Vector2(0, 0), false);
        arenaLogic = new ArenaLogic(this);

        initArenaPhysics();

        hero = new Hero(arenaWorld, 0, -3f);
        enemies = new com.badlogic.gdx.utils.Array<Enemy>();

        Gdx.input.setInputProcessor(hero.getInputProcessor());

        box2DDebugRenderer = new Box2DDebugRenderer();
        myCam = new MyOrthoCam(Gdx.graphics.getWidth()/SCALE, Gdx.graphics.getHeight()/SCALE);
        myCam.zoom = zoom;
        tweenManager = new TweenManager();
        Tween.registerAccessor(OrthographicCamera.class, new CamAccessor());

        worldSprite = new Sprite(Assets.world);
        worldSprite.setSize(Assets.world.getWidth() * 2f / SCALE, Assets.world.getHeight() *2f / SCALE);
        worldSprite.setOriginCenter();

        backgroundLayers = new Sprite[5];
        backgroundLayers[0] = new Sprite(Assets.layer1);
        backgroundLayers[1] = new Sprite(Assets.layer2);
        backgroundLayers[2] = new Sprite(Assets.layer3);
        backgroundLayers[3] = new Sprite(Assets.layer04_middle);
        backgroundLayers[4] = new Sprite(Assets.map);

        backgroundLayers[0].setSize(Assets.layer1.getWidth() * 2f / SCALE, Assets.layer1.getHeight() * 2f / SCALE);
        backgroundLayers[0].setOriginCenter();

        backgroundLayers[1].setSize(Assets.layer2.getWidth() / SCALE, Assets.layer2.getHeight() / SCALE);
        backgroundLayers[1].setPosition(-backgroundLayers[1].getWidth() / 2
                                      , -backgroundLayers[1].getHeight() / 2f);

        float scaleLayer5 = 3.5f;
        backgroundLayers[4].setSize(Assets.map.getWidth() * scaleLayer5 / SCALE
                                  , Assets.map.getHeight() * scaleLayer5 / SCALE);
        backgroundLayers[4].setOriginCenter();
        backgroundLayers[4].setColor(0.2f, 0.2f, 0.2f, 1f);

    }
    private void initArenaPhysics(){
        float worldYDisplacement = -3f - Hero.HeroConstants.HEIGHT / 2f;

        // Ground init
        groundBodyDef = new BodyDef();
        groundBodyDef.position.set(0, worldYDisplacement);
        Body groundBody = arenaWorld.createBody(groundBodyDef);
        groundBody.setType(BodyDef.BodyType.StaticBody);
        groundShape = new ChainShape();
        groundShape.createChain(new Vector2[]{new Vector2(-Constants.WORLD_WIDTH / 2f, 0), new Vector2(Constants.WORLD_WIDTH / 2f, 0)});
        groundBody.createFixture(groundShape, 1f);

        // Wall left init
        leftWallBodyDef = new BodyDef();
        leftWallBodyDef.position.set( - Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2f + worldYDisplacement);
        Body leftWallBody = arenaWorld.createBody(leftWallBodyDef);
        leftWallBody.setType(BodyDef.BodyType.StaticBody);
        leftWallShape = new ChainShape();
        leftWallShape.createChain(new Vector2[]{new Vector2(0, - Constants.WORLD_HEIGHT / 2f), new Vector2(0, Constants.WORLD_HEIGHT / 2f)});
        leftWallBody.createFixture(leftWallShape, 1f);

        // Wall right init
        rightWallBodyDef = new BodyDef();
        rightWallBodyDef.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2f + worldYDisplacement);
        Body rightWallBody = arenaWorld.createBody(rightWallBodyDef);
        rightWallBody.setType(BodyDef.BodyType.StaticBody);
        rightWallShape = leftWallShape;
        rightWallBody.createFixture(rightWallShape, 1f);

        groundShape.dispose();
        leftWallShape.dispose();
    }
    private void camUpdate(){
        float angle = MathUtils.sin(MathUtils.PI2 * time / timeStretch) * 2f;
        myCam.setAngle(angle);


        //myCam.rotate(MathUtils.cos( MathUtils.PI * time / timeStretch /60f) * 0.006f);
        // myCam.zoom = (MathUtils.randomBoolean(0.5f)) ? MathUtils.random(-0.002f, 0.002f) + zoom : myCam.zoom;
        float posDiff = Math.abs(myCam.position.x - hero.getPosition().x);
        float camYDisplacement = MathUtils.cos(MathUtils.PI2 * time / timeStretch) * 2f;
        myCam.position.y = hero.getPosition().y + camYDisplacement;
        if(posDiff >= camOffset ) {
            isFollowing = true;
            if (tweenManager.getRunningTweensCount() == 0) tweenManager.killAll();
            if(myCam.position.x > hero.getPosition().x) {
                Tween.to(myCam, CamAccessor.POSITION, 0.02f)
                         .target(hero.getPosition().x + camOffset - 0.1f)
                         .start(tweenManager);
            }else if(myCam.position.x < hero.getPosition().x) {
                Tween.to(myCam, CamAccessor.POSITION, 0.02f)
                         .target(hero.getPosition().x - camOffset + 0.1f)
                         .start(tweenManager);
            }
        }else if(isFollowing && posDiff < camOffset ){
            tweenManager.killAll();
            Tween.to(myCam, CamAccessor.POSITION, 1f)
                    .target(hero.getPosition().x )
                    .ease(TweenEquations.easeOutBack)
                    .start(tweenManager);
            isFollowing = false;
        }
        myCam.update();
    }

    /*
    private ContactListener implementContactListener() {
        ContactListener contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                boolean condition1 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                                  && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                boolean condition2 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                                  && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);

               // if (condition1 || condition2) handleBodyContact();  //checks if there is collision at all

            }

            private void handleBodyContact() {
                Gdx.app.log("EnemyX" , String.valueOf(enemy.getPosition().x));
                Gdx.app.log("HeroX" , String.valueOf(hero.getPosition().x));

                if (hero.getPosition().x > enemy.getPosition().x){
                    if(hero.getOrientation() == Hero.Orientation.LEFT) {
                        hero.stop();
                        hero.setVelocity(0, Hero.HeroConstants.VELOCITY);
                    }
                }
                if (hero.getPosition().x < enemy.getPosition().x){
                    if(hero.getOrientation() == Hero.Orientation.RIGHT) {
                        hero.stop();
                        hero.setVelocity(Hero.HeroConstants.VELOCITY, 0);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                boolean condition1 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                                  && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                boolean condition2 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                                  && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);


                if (condition1 || condition2)
                    hero.setVelocity(Hero.HeroConstants.VELOCITY, Hero.HeroConstants.VELOCITY);

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                boolean condition1 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                        && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                boolean condition2 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                        && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);


            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        return contactListener;
    }
    */

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        arenaWorld.step(1 / 60f, 8, 3);
        tweenManager.update(delta);
        time += delta;
        camUpdate();
        updateBackground();
        hero.update();

        GlobalTools.batch.setProjectionMatrix(myCam.combined);
        GlobalTools.batch.begin();
        backgroundLayers[0].draw(GlobalTools.batch);
        worldSprite.draw(GlobalTools.batch);
        backgroundLayers[1].draw(GlobalTools.batch);
        hero.draw(GlobalTools.batch);
        backgroundLayers[4].draw(GlobalTools.batch);
        GlobalTools.batch.end();



        box2DDebugRenderer.render(arenaWorld, myCam.combined);
    }
    public void updateBackground(){
        backgroundLayers[0].setPosition(myCam.position.x - backgroundLayers[0].getWidth() / 2f
                , myCam.position.y *0.5f - backgroundLayers[0].getHeight() / 2f);

        backgroundLayers[4].setPosition((-myCam.position.x * 0.3f) - backgroundLayers[4].getWidth() / 2f
                , -backgroundLayers[4].getHeight() / 2f + MathUtils.cos(MathUtils.PI2 * time / timeStretch) * 3f);

        worldSprite.setPosition(myCam.position.x * 0.98f + myCam.viewportWidth * 0.3f * zoom
                , MathUtils.cos(MathUtils.PI2 * time / timeStretch) * 1.5f + myCam.position.y + myCam.viewportHeight * 0.3f * zoom);

        worldSprite.rotate(-0.01f);
       // backgroundLayers[0].rotate(-0.03f);


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
        arenaWorld.dispose();
        box2DDebugRenderer.dispose();

    }

}
