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

    private int health = 150;

    public enum Orientation{LEFT, RIGHT;};
    private Orientation orientation = Orientation.LEFT;

    public Enemy(World world, float spawnX, float spawnY){
        super();
        setPosition(spawnX, spawnY);
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

    public void gotHit(){
        health -= 10;
    }

    /*  Getters and Setters */
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
