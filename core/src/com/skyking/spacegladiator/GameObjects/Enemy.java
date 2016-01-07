package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Gök on 07.01.2016.
 */
public class Enemy extends Entity2D {
    private World world;
    EnemyPhysics enemyPhysics;
    EnemyDrawer enemyDrawer;

    public enum State{IDLE, RUNNING, ATTACK};
    private State state = State.IDLE;

    public enum Orientation{LEFT, RIGHT;};
    private Orientation orientation = Orientation.LEFT;

    public Enemy(World world){
        super();
        setPosition(getPosition().x - 10f, getPosition().y);
        setSize(Hero.HeroConstants.WIDTH, Hero.HeroConstants.HEIGHT);
        this.world = world;
        enemyPhysics = new EnemyPhysics(this);
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void update() {
        setPosition(enemyPhysics.getPosition());
    }

    public World getWorld() {
        return world;
    }

    public State getState() {
        return state;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setState(State state) {
        this.state = state;
    }

    public EnemyPhysics getEnemyPhysics() {
        return enemyPhysics;
    }
}
