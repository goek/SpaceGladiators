package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.WorldListener;

/**
 * Created by Gök on 13.01.2016.
 */
public class Rovenade extends Entity2D {

    private float velocityL = Constants.VELOCITY,
                  velocityR = Constants.VELOCITY;

    private WorldListener worldListener;

    public void setWorldListener(WorldListener worldListener) {
        this.worldListener = worldListener;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public RovenadePhysics getRovenadePhysics() {
        return rovenadePhysics;
    }

    public void deadAndBury() {
        worldListener.notifyRovenadeDeath(this);
        world.destroyBody(rovenadePhysics.getFixture().getBody());
    }


    public enum State{SPAWN, DRIVE, ONCONTACT;};
    private State state;

    public enum Orientation{LEFT, RIGHT;};

    private RovenadePhysics rovenadePhysics;
    private RovenadeDrawer rovenadeDrawer;

    private World world;
    private Orientation orientation;

    public Rovenade(World world, float spawnX, float spawnY, Orientation orientation){
        super();
        this.world = world;
        this.orientation = orientation;
        setPosition(spawnX, spawnY);
        setSize(3f, 6f);

        rovenadeDrawer = new RovenadeDrawer(this);
        rovenadePhysics = new RovenadePhysics(this);
        state = State.SPAWN;
    }

    @Override
    public void draw(SpriteBatch batch) {
        rovenadeDrawer.draw(batch);
    }

    @Override
    public void update() {
        setPosition(rovenadePhysics.getPosition());
        rovenadeDrawer.update();
        rovenadePhysics.update();
    }

    public World getWorld() {
        return world;
    }

    public void drive(Orientation orientation){
        this.orientation = orientation;
        setState(State.DRIVE);
        switch (orientation) {
            case LEFT:
                rovenadePhysics.setLinearVelocity(-velocityL, 0);
                break;
            case RIGHT:
                rovenadePhysics.setLinearVelocity(velocityR, 0);
                break;
        }
    }

    public void stop(){
        rovenadePhysics.setLinearVelocity(0, 0);
    }

    public static class Constants{
        // defining hero size based on IDLE Assets
        public static final float textureRatio = Assets.rovenadeSpawnAtlas.getRegions().first().getRotatedPackedHeight()
                                                / Assets.rovenadeSpawnAtlas.getRegions().first().getRotatedPackedWidth();


        public static final float WIDTH = 4f;
        public static final float HEIGHT = textureRatio* WIDTH;

        public static final float VELOCITY = 40f;

    }
}

