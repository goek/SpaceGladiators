package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.skyking.spacegladiator.WorldListener;
import com.skyking.spacegladiator.util.Tools;

/**
 * Created by Gök on 07.01.2016.
 */
public class Enemy extends Entity2D {
    private World world;
    EnemyPhysics enemyPhysics;
    EnemyDrawer enemyDrawer;
    EnemyBrain enemyBrain;

    private float velocityL = Hero.Constants.VELOCITY;
    private float velocityR = Hero.Constants.VELOCITY;

    public enum State{IDLE, RUNNING, ATTACK, DIE};

    private State state = State.IDLE;
    private int healthPoints = 3;
    public enum Orientation{LEFT, RIGHT;};

    private WorldListener deathListener;


    private Orientation orientation = Orientation.LEFT;

    public Enemy(World world, float spawnX, float spawnY){
        super();
        setPosition(spawnX, spawnY);
        setSize(Hero.Constants.WIDTH, Hero.Constants.HEIGHT);
        this.world = world;
        enemyPhysics = new EnemyPhysics(this);
        enemyDrawer = new EnemyDrawer(this);
        enemyBrain = new EnemyBrain(this);
    }

    @Override
    public void draw(SpriteBatch batch) {
        enemyDrawer.draw(Tools.batch);
    }

    @Override
    public void update() {
        setPosition(enemyPhysics.getPosition());
        enemyPhysics.update();
        enemyBrain.update();
        enemyDrawer.update();
    }

    public void gotHit(boolean fatality){
        if(fatality){
            healthPoints = 0;
            stop();
            setState(State.DIE);
            return;
        }

        healthPoints--;
        if(healthPoints <= 0){
            stop();
            setState(State.DIE);
        }
    }

    public void stop() {
        if(state==State.DIE) return;
        setState(State.IDLE);
        enemyPhysics.setLinearVelocity(0, 0);
    }

    public void punch(){
        if(state==State.DIE) return;
        enemyPhysics.setLinearVelocity(0, 0);
        enemyPhysics.useWeapon();
        setState(State.ATTACK);
    }

    public void run(Orientation orientation){
        if(state==State.DIE) return;
        this.orientation = orientation;
        setState(State.RUNNING);
        switch (orientation) {
            case LEFT:
                enemyPhysics.setLinearVelocity(-velocityL, 0);
                break;
            case RIGHT:
                enemyPhysics.setLinearVelocity(velocityR, 0);
                break;
            }
    }

    public void deadAndBury(){
        world.destroyBody(enemyPhysics.getFixture(EnemyPhysics.FixtureNames.BODY).getBody());
        world.destroyBody(enemyPhysics.getFixture(EnemyPhysics.FixtureNames.WEAPON).getBody());
        deathListener.notifyEnemyDeath(this);
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

    @Override
    public void setPosition(Vector2 position) {
        super.setPosition(position);
        enemyPhysics.setPosition(position);
    }

    public EnemyPhysics getEnemyPhysics() {
        return enemyPhysics;
    }

    public EnemyBrain getEnemyBrain() {
        return enemyBrain;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setDeathListener(WorldListener deathListener) {
        this.deathListener = deathListener;
    }
}
