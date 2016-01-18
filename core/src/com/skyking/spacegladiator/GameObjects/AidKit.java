package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.skyking.spacegladiator.util.AnimationManager;

/**
 * Created by Gök on 17.01.2016.
 */
public class AidKit extends Entity2D{
    public enum State{ONSPAWN, IDLE, PICKUP}
    private State state = State.ONSPAWN;
    private AnimationManager<State> animationManager;
    private World world;
    private Body body;

    public AidKit(World world, float spawnX, float spawnY){
        super();
        this.world = world;
        setPosition(spawnX, spawnY);
        animationManager = new AnimationManager<State>();

        initBody();
        initAnimation();
    }

    private void initAnimation() {

    }

    private void initBody() {

    }

    @Override
    public void draw(SpriteBatch batch) {
        switch (state) {
            case ONSPAWN:
                break;
            case IDLE:
                break;
            case PICKUP:
                break;
        }
    }

    @Override
    public void update() {

    }
}
