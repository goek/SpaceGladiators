package com.skyking.spacegladiator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.CamAccessor;
import com.skyking.spacegladiator.GameObjects.Enemy;
import com.skyking.spacegladiator.GameObjects.EnemyPhysics;
import com.skyking.spacegladiator.GameObjects.Hero;
import com.skyking.spacegladiator.GameObjects.HeroDrawer;
import com.skyking.spacegladiator.GameObjects.HeroPhysics;
import com.skyking.spacegladiator.GameObjects.Rovenade;
import com.skyking.spacegladiator.util.Constants;
import com.skyking.spacegladiator.util.Tools;
import com.skyking.spacegladiator.util.MyOrthoCam;

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
    private BitmapFont bitmapFont;

    private Sprite[] backgroundLayers;
    private Sprite worldSprite;

    private Hero hero;
    private World arenaWorld;
    private BodyDef groundBodyDef, leftWallBodyDef, rightWallBodyDef;
    private ChainShape groundShape, leftWallShape, rightWallShape;
    private Array<Body> worldBoundaryBodies;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Array<Enemy> enemies;
    private Array<Rovenade> rovenades;
    
    TweenManager tweenManager;
    private ArenaLogic arenaLogic;

    private Stage hudStage;

    public static final float SCALE = 20f ;

    public ArenaScreen(){
        initArenaScreen();
        time = 0;

        Gdx.input.setCatchBackKey(false);
    }

    private void initArenaScreen() {
        arenaWorld = new World(new Vector2(0, 0), false);

        initArenaPhysics();
        initHUD();

        hero = new Hero(arenaWorld, 0, -3f);
        enemies = new Array<Enemy>();
        rovenades = new Array<Rovenade>();
        
        arenaWorld.setContactListener(implementContactListener());
        arenaLogic = new ArenaLogic(this);

        Gdx.input.setInputProcessor(hero.getInputProcessor());

        box2DDebugRenderer = new Box2DDebugRenderer();
        myCam = new MyOrthoCam(Gdx.graphics.getWidth()/SCALE, Gdx.graphics.getHeight()/SCALE);
        myCam.zoom = zoom;
        tweenManager = new TweenManager();
        Tween.registerAccessor(OrthographicCamera.class, new CamAccessor());

        worldSprite = new Sprite(Assets.world);
        worldSprite.setSize(Assets.world.getWidth() * 2f / SCALE, Assets.world.getHeight() * 2f / SCALE);
        worldSprite.setOriginCenter();

        backgroundLayers = new Sprite[5];
        backgroundLayers[0] = new Sprite(Assets.layer1);
        backgroundLayers[1] = new Sprite(Assets.layer2);
        backgroundLayers[2] = new Sprite(Assets.layer3);
        backgroundLayers[3] = new Sprite(Assets.layer04_middle);
        backgroundLayers[4] = new Sprite(Assets.map);

        backgroundLayers[0].setSize(Assets.layer1.getWidth() * 2f / SCALE, Assets.layer1.getHeight() * 2f / SCALE);
        backgroundLayers[0].setOriginCenter();

        backgroundLayers[1].setSize(Assets.layer2.getWidth() / SCALE
                , Assets.layer2.getHeight() / SCALE);
        backgroundLayers[1].setPosition(-backgroundLayers[1].getWidth() / 2f
                , -backgroundLayers[1].getHeight() / 2f);

        float scaleLayer5 = 3.5f;
        backgroundLayers[4].setSize(Assets.map.getWidth() * scaleLayer5 / SCALE
                , Assets.map.getHeight() * scaleLayer5 / SCALE);
        backgroundLayers[4].setOriginCenter();
        backgroundLayers[4].setColor(0.2f, 0.2f, 0.2f, 1f);

    }

    private void initHUD() {
        bitmapFont = new BitmapFont();
        bitmapFont.getData().scale(2);
        bitmapFont.setColor(Color.RED);
        Actor textActor = new Actor(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                bitmapFont.draw(batch, String.valueOf(arenaLogic.getKillCount()), Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight() * 0.9f);
                bitmapFont.draw(batch, String.valueOf(hero.getRovenades()), Gdx.graphics.getWidth() * 0.9f, Gdx.graphics.getHeight() * 0.9f);
                bitmapFont.draw(batch, String.valueOf(hero.getRovenades()), Gdx.graphics.getWidth() * 0.9f, Gdx.graphics.getHeight() * 0.9f);
                if (arenaLogic.getGameState() == ArenaLogic.GameState.STOP)
                bitmapFont.draw(batch, "You are Defeated! Press Space to start new Round", Gdx.graphics.getWidth() * 0.1f, Gdx.graphics.getHeight()* 0.5f);

            }
        };
        hudStage = new Stage();
        hudStage.addActor(HeroDrawer.HealthBar.getInstance());
        hudStage.addActor(textActor);
    }

    private void initArenaPhysics(){
        float worldYDisplacement = -3f - Hero.Constants.HEIGHT / 2f;

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
        leftWallBodyDef.position.set(-Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2f + worldYDisplacement);
        Body leftWallBody = arenaWorld.createBody(leftWallBodyDef);
        leftWallBody.setType(BodyDef.BodyType.StaticBody);
        leftWallShape = new ChainShape();
        leftWallShape.createChain(new Vector2[]{new Vector2(0, -Constants.WORLD_HEIGHT / 2f), new Vector2(0, Constants.WORLD_HEIGHT / 2f)});
        leftWallBody.createFixture(leftWallShape, 1f);

        // Wall right init
        rightWallBodyDef = new BodyDef();
        rightWallBodyDef.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2f + worldYDisplacement);
        Body rightWallBody = arenaWorld.createBody(rightWallBodyDef);
        rightWallBody.setType(BodyDef.BodyType.StaticBody);
        rightWallShape = leftWallShape;
        rightWallBody.createFixture(rightWallShape, 1f);

        worldBoundaryBodies = new Array<Body>();
        worldBoundaryBodies.add(groundBody);
        worldBoundaryBodies.add(leftWallBody);
        worldBoundaryBodies.add(rightWallBody);
        
        groundShape.dispose();
        leftWallShape.dispose();
    }
    private void camUpdate(){
        float camOffset = 10f;
        float angle = MathUtils.sin(MathUtils.PI2 * time / timeStretch) * 2f;
        myCam.setAngle(angle);


        //myCam.rotate(MathUtils.cos( MathUtils.PI * time / timeStretch /60f) * 0.006f);
        // myCam.zoom = (MathUtils.randomBoolean(0.5f)) ? MathUtils.random(-0.002f, 0.002f) + zoom : myCam.zoom;
        float posDiff = Math.abs(myCam.position.x - hero.getPosition().x);
        float camYDisplacement = MathUtils.cos(MathUtils.PI2 * time / timeStretch) * 1.6f ;
        myCam.position.y =  camYDisplacement;
        if(posDiff >= camOffset) {
            isFollowing = true;
            if (tweenManager.getRunningTweensCount() == 0) tweenManager.killAll();
            if(myCam.position.x > hero.getPosition().x) {
                Tween.to(myCam, CamAccessor.POSITION, 0.04f)
                         .target(hero.getPosition().x + camOffset - 0.4f)
                         .start(tweenManager);
            }else if(myCam.position.x < hero.getPosition().x) {
                Tween.to(myCam, CamAccessor.POSITION, 0.04f)
                         .target(hero.getPosition().x - camOffset + 0.4f)
                         .start(tweenManager);
            }
        }else if(isFollowing && posDiff < camOffset){
            tweenManager.killAll();
            Tween.to(myCam, CamAccessor.POSITION, 1f)
                    .target(hero.getPosition().x )
                    .ease(TweenEquations.easeOutBack)
                    .start(tweenManager);
            isFollowing = false;
        }
        myCam.update();
    }


    private ContactListener implementContactListener() {
        ContactListener contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                for(Enemy enemy : enemies) {
                    if (enemy.getState() == Enemy.State.DIE) return;
                    boolean condition1 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                    boolean condition2 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);

                    if (condition1 || condition2) handleBodyContact(enemy);  //checks if there is collision at all

                    boolean condition3 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.WEAPON)
                            && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                    boolean condition4 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.WEAPON);

                    if ((condition3 || condition4) && hero.getState() == Hero.State.ATTACK) handleHeroPunchContact(enemy);

                    boolean condition5 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.WEAPON);
                    boolean condition6 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.WEAPON)
                            && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);

                    if (condition5 || condition6) hero.gotHit();

                    for(Rovenade rovenade : rovenades) {
                        boolean condition7 = contact.getFixtureA() == rovenade.getRovenadePhysics().getFixture()
                                && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                        boolean condition8 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                                && contact.getFixtureB() == rovenade.getRovenadePhysics().getFixture();

                        if ((condition7 || condition8) && rovenade.getState() != Rovenade.State.ONCONTACT){
                            rovenade.stop();
                            rovenade.setState(Rovenade.State.ONCONTACT);
                            enemy.gotHit(true);
                        };


                    }
                }
            }

            private void handleHeroPunchContact(Enemy enemy) {
                enemy.gotHit(false);

            }

            private void handleBodyContact(Enemy enemy) {

                if (hero.getPosition().x > enemy.getPosition().x){
                    if(hero.getOrientation() == Hero.Orientation.LEFT) {
                        hero.stop();
                        hero.setVelocity(0, Hero.Constants.VELOCITY);
                    }
                }
                if (hero.getPosition().x < enemy.getPosition().x){
                    if(hero.getOrientation() == Hero.Orientation.RIGHT) {
                        hero.stop();
                        hero.setVelocity(Hero.Constants.VELOCITY, 0);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                for (Enemy enemy : enemies) {

                    boolean condition1 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                    boolean condition2 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);


                    if (condition1 || condition2)
                        hero.setVelocity(Hero.Constants.VELOCITY, Hero.Constants.VELOCITY);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }




            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        return contactListener;
    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        updateMethods(delta);
        drawingMethods();
    }

    private void drawingMethods(){
        Tools.batch.setProjectionMatrix(myCam.combined);
        Tools.batch.begin();
        backgroundLayers[0].draw(Tools.batch);
        worldSprite.draw(Tools.batch);
        backgroundLayers[1].draw(Tools.batch);
        try {
            for (Enemy enemie : enemies) {
                enemie.draw(Tools.batch);
            }
        }catch(Exception e){}
        hero.draw(Tools.
                batch);
        for (Rovenade rovenade : rovenades){
            rovenade.draw(Tools.batch);
        }
        backgroundLayers[4].draw(Tools.batch);
        Tools.batch.end();
        hudStage.draw();


        //box2DDebugRenderer.render(arenaWorld, myCam.combined);
    }

    private void updateMethods(float delta){
        time += delta;
        arenaLogic.update(delta);
        arenaWorld.step(1 / 60f, 8, 3);
        hudStage.act(delta);
        for(Enemy enemy : enemies){
            enemy.update();  }
        for(Rovenade rovenade : rovenades){
            rovenade.update();
        }
        hero.update();

        tweenManager.update(delta);
        camUpdate();
        updateBackground();
    }

    public void updateBackground(){
        backgroundLayers[0].setPosition(myCam.position.x - backgroundLayers[0].getWidth() / 2f
                                      , myCam.position.y *0.4f - backgroundLayers[0].getHeight() / 2f);

        backgroundLayers[1].setY(myCam.position.y * 0.7f - backgroundLayers[1].getHeight() / 2f);

        backgroundLayers[4].setPosition((-myCam.position.x * 0.3f) - backgroundLayers[4].getWidth() / 2f
                , -backgroundLayers[4].getHeight() / 2f - myCam.position.y * 1.5f);

        worldSprite.setPosition(myCam.position.x * 0.98f + myCam.viewportWidth * 0.3f * zoom
                , myCam.position.y * 0.5f + myCam.position.y + myCam.viewportHeight * 0.3f * zoom);

        worldSprite.rotate(-0.01f);
        backgroundLayers[0].rotate(-0.03f);


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
        hudStage.dispose();
        bitmapFont.dispose();

    }

    public Hero getHero() {
        return hero;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }

    public World getWorld() {
        return arenaWorld;
    }

    public Array<Body> getWorldBoundaryBodies() {
        return worldBoundaryBodies;
    }

    public Array<Rovenade> getRovenades() {
        return rovenades;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}
