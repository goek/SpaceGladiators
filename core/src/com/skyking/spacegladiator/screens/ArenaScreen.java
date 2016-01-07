package com.skyking.spacegladiator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.GameObjects.Enemy;
import com.skyking.spacegladiator.GameObjects.EnemyPhysics;
import com.skyking.spacegladiator.GameObjects.Hero;
import com.skyking.spacegladiator.GameObjects.HeroPhysics;
import com.skyking.spacegladiator.util.GlobalTools;

/**
 * Created by Gök on 02.01.2016.
 */
public class ArenaScreen implements Screen {
    private World arenaWorld;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthographicCamera myCam;
    private Sprite[] backgroundLayers;
    private Hero hero;
    private Enemy enemy;

    private static final float SCALE = 20f;

    public ArenaScreen(){
        initArenaScreen();
    }

    private void initArenaScreen() {
        arenaWorld = new World(new Vector2(0, 0), false);
        hero = new Hero(arenaWorld);
        enemy = new Enemy(arenaWorld);

        arenaWorld.setContactListener(implementContactListener());
        Gdx.input.setInputProcessor(hero.getInputProcessor());

        box2DDebugRenderer = new Box2DDebugRenderer();
        myCam = new OrthographicCamera(Gdx.graphics.getWidth()/SCALE, Gdx.graphics.getHeight()/SCALE);
       // Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        backgroundLayers = new Sprite[3];
        backgroundLayers[0] = new Sprite(Assets.layer1);
        backgroundLayers[1] = new Sprite(Assets.layer2);
        backgroundLayers[2] = new Sprite(Assets.layer3);

    }

    private ContactListener implementContactListener() {
        ContactListener contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                boolean condition1 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                                  && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                boolean condition2 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                                  && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);

                if (condition1 || condition2) handleBodyContact();  //checks if there is collision at all

            }

            private void handleBodyContact() {
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
        arenaWorld.step(1/60f,8,3);
        hero.update();
        enemy.update();

        box2DDebugRenderer.render(arenaWorld, myCam.combined);

        GlobalTools.batch.setProjectionMatrix(myCam.combined);
        GlobalTools.batch.begin();
        hero.draw(GlobalTools.batch);
        GlobalTools.batch.end();



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
