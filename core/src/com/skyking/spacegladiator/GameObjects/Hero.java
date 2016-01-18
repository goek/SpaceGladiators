package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.WorldListener;

/**
 * Created by Gök on 04.01.2016.
 */
public class Hero extends Entity2D {
    private World world;
    private HeroPhysics heroPhysics;
    private HeroDrawer heroDrawer;
    private HeroInput heroInput;
    private WorldListener worldListener;

    public void setWorldListener(WorldListener worldListener) {
        this.worldListener = worldListener;
    }

    private float velocityL = Constants.VELOCITY;               //2 velocity values for collision detection
    private float velocityR = Constants.VELOCITY;
    public float jumpVelocity = Constants.VELOCITY;

    private int health = 150;
    public static final int MAXHEALTH = 150;

    public static final int MAXROVENADES = 5;

    public int getRovenades() {
        return rovenades;
    }

    public void setRovenades(int rovenades) {
        this.rovenades = rovenades;
    }

    private int rovenades = MAXROVENADES;

    private Array<Rovenade> usedRovenades;

    public enum State{IDLE, RUNNING, ATTACK, JUMP, DIE};
    private State state = State.IDLE;

    public enum Orientation{LEFT, RIGHT;};
    private Orientation orientation = Orientation.LEFT;


    public Hero(World world, float spawnPosX, float spawnPosY){
        super();
        usedRovenades = new Array<Rovenade>();

        setPosition(spawnPosX, spawnPosY);
        setSize(Constants.WIDTH, Constants.HEIGHT);
        this.world = world;
        heroPhysics = new HeroPhysics(this);
        heroDrawer = new HeroDrawer(this);
        heroInput = new HeroInput(this);
    }

    @Override
    public void draw(SpriteBatch batch) {
        heroDrawer.draw(batch);

    }

    @Override
    public void update() {
        setPosition(heroPhysics.getPosition());
        heroPhysics.update();
        heroInput.update();
        heroDrawer.update();

    }

    /*  Actions for our Hero    */
    public void punch(){
        heroPhysics.setLinearVelocity(0, 0);
        heroPhysics.useWeapon();
        setState(State.ATTACK);
    }

    public void run(Orientation orientation){
        this.orientation = orientation;
        setState(State.RUNNING);
        switch (orientation) {
            case LEFT:
                heroPhysics.setLinearVelocity(-velocityL, 0);
                break;
            case RIGHT:
                heroPhysics.setLinearVelocity(velocityR, 0);
                break;
        }
    }

    public void stop() {
         setState(State.IDLE);
         heroPhysics.setLinearVelocity(0, 0);
    }

    public void useRovenade(){
        if (rovenades <=0) return;

        switch (getOrientation()) {
            case LEFT:
                worldListener.notifyRovenadeSpawn(new Rovenade(world, getPosition().x
                        , -3f - Hero.Constants.HEIGHT / 2f + Rovenade.Constants.HEIGHT / 2f, Rovenade.Orientation.LEFT));
                break;
            case RIGHT:
                worldListener.notifyRovenadeSpawn(new Rovenade(world, getPosition().x
                        , -3f - Hero.Constants.HEIGHT / 2f + Rovenade.Constants.HEIGHT / 2f, Rovenade.Orientation.RIGHT));
                break;
        }
        rovenades --;
    }



    public void gotHit(){
        health -= 10;
        if (health <= 0) {
            setState(State.DIE);
            worldListener.notifyHeroDeath();
        }
    }

    /*  Getters and Setters  */
    public World getWorld() {
        return world;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public InputProcessor getInputProcessor(){
        return heroInput;
    }

    public State getState() {
        return state;
    }

    public HeroPhysics getHeroPhysics() {
        return heroPhysics;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setVelocity(float velocityL, float velocityR) {
        this.velocityL = velocityL;
        this.velocityR = velocityR;
    }

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        heroPhysics.setPosition(position);
    }

    public int getHealth() {
        return health;
    }

    public static final class Constants {
        // defining hero size based on IDLE Assets
        public static final float idleTextureRatio2 = Assets.destIdleAtlas.getRegions().first().getRotatedPackedHeight()
                                                    / Assets.destIdleAtlas.getRegions().first().getRotatedPackedWidth();


        public static final float WIDTH = 5f;
        public static final float HEIGHT = idleTextureRatio2 * WIDTH;


        public static final float VELOCITY = 50f;
        public static final float PUNCHVELOCITY = 60f;
        public static final float JUMPVELOCITY = 10f;

    }
}
