package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.skyking.spacegladiator.Assets;

/**
 * Created by Gök on 04.01.2016.
 */
public class Hero extends Entity2D {
    private World world;
    private HeroPhysics heroPhysics;
    private HeroDrawer heroDrawer;
    private HeroInput heroInput;

    private float velocityL = HeroConstants.VELOCITY;               //2 velocity values for collision detection
    private float velocityR = HeroConstants.VELOCITY;
    public float jumpVelocity = HeroConstants.VELOCITY;

    private int health = 150;

    public enum State{IDLE, RUNNING, ATTACK, JUMP};
    private State state = State.IDLE;

    public enum Orientation{LEFT, RIGHT;};
    private Orientation orientation = Orientation.LEFT;


    public Hero(World world, float spawnPosX, float spawnPosY){
        super();
        setPosition(spawnPosX, spawnPosY);
        setSize(HeroConstants.WIDTH, HeroConstants.HEIGHT);
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

    public void jump(){
        setState(State.JUMP);
        heroPhysics.applyForce(0, HeroConstants.JUMPVELOCITY);
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





    /*  Inner Classes  */
    public static final class HeroConstants {
        // defining hero size based on IDLE Assets
        public static final float idleTextureRatio = Assets.destIdleAtlas.getRegions().first().getRegionHeight()
                                                   / Assets.destIdleAtlas.getRegions().first().getRegionWidth();

        public static final float idleTextureRatio2 = Assets.destIdleAtlas.getRegions().first().getRotatedPackedHeight()
                                                    / Assets.destIdleAtlas.getRegions().first().getRotatedPackedWidth();


        public static final float WIDTH = 5f;
        public static final float HEIGHT = idleTextureRatio2 * WIDTH;


        public static final float VELOCITY = 35f;
        public static final float PUNCHVELOCITY = 40f;
        public static final float JUMPVELOCITY = 10f;

    }
}
