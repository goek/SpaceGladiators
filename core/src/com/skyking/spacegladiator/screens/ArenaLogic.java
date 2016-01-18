package com.skyking.spacegladiator.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.skyking.spacegladiator.GameObjects.Rovenade;
import com.skyking.spacegladiator.WorldListener;
import com.skyking.spacegladiator.GameObjects.Enemy;
import com.skyking.spacegladiator.GameObjects.EnemyPhysics;
import com.skyking.spacegladiator.GameObjects.Hero;
import com.skyking.spacegladiator.GameObjects.HeroPhysics;
import com.skyking.spacegladiator.util.Constants;

/**
 * Created by Gök on 11.01.2016.
 */
public class ArenaLogic extends InputAdapter{
    private ArenaScreen arenaScreen;
    private Hero hero;
    private Array<Enemy> enemies;
    private Array<Rovenade> rovenades;
    private World world;
    private WorldListener worldListener;
    private Array<Body> worldBoundaryBodies;
    private boolean dieContactHandled = false;
    private int killCount = 0;

    Contact lastDieContact = null;

    public int getKillCount() {
        return killCount;
    }

    public GameState getGameState() {
        return gameState;
    }

    public enum GameState{BEGINNING, STOP, RUNNING, ROUND1, ROUND2;}

    public Array<Rovenade> getRovenades() {
        return rovenades;
    }

    private GameState gameState = GameState.BEGINNING;

    private Array<Enemy> nearestEnemies= new Array<Enemy>(2);
    public ArenaLogic(ArenaScreen arenaScreen){
        this.arenaScreen = arenaScreen;
        this.hero = arenaScreen.getHero();
        this.enemies = arenaScreen.getEnemies();
        this.world = arenaScreen.getWorld();
        this.worldBoundaryBodies = arenaScreen.getWorldBoundaryBodies();
        this.rovenades = arenaScreen.getRovenades();

        worldListener = new WorldListener(this);
        hero.setWorldListener(worldListener);
    }

    public void stopGame() {
        Gdx.input.setInputProcessor(this);
        setGameState(GameState.STOP);

    }

    public void update(float delta){

        switch (gameState) {
            case BEGINNING:
                spawnEnemy();
                spawnEnemy();
                spawnEnemy();
                setGameState(GameState.RUNNING);
                break;
            case RUNNING:
                updateNearestEnemys();
                if(enemies.size < 3) spawnEnemy();
                contactUpdate();
                break;
            case ROUND1:
                break;
            case ROUND2:
                break;
            case STOP:

                break;
        }
    }

    private void updateNearestEnemys(){
        for(Enemy enemy : enemies){
            enemy.getEnemyBrain().setHasDirectSight(false);
        }

        for(Enemy enemy : getNearestEnemies()){

            enemy.getEnemyBrain().setHasDirectSight(true);
            enemy.getEnemyBrain().delegateHeroPosition(hero.getPosition());
        }
    }


    private void contactUpdate() {

        Array<Contact> contactList = world.getContactList();
        for (Contact contact : contactList) {
            for (Enemy enemy : enemies) {
                if (enemy.getState() == Enemy.State.DIE) {
                    if (contact != lastDieContact) dieContactHandled = false;

                    boolean condition1 = contact.getFixtureA() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY);
                    boolean condition2 = contact.getFixtureA() == enemy.getEnemyPhysics().getFixture(EnemyPhysics.FixtureNames.BODY)
                            && contact.getFixtureB() == hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY);

                    if (condition1 || condition2 && !dieContactHandled) {
                        hero.setVelocity(Hero.Constants.VELOCITY, Hero.Constants.VELOCITY);
                        lastDieContact = contact;
                        dieContactHandled = true;
                    }
                }
            }
        }
    }

    // index 0 = left enemy from hero, index = 1 right enemy
    private Array<Enemy> getNearestEnemies() {
        nearestEnemies.clear();
        Enemy nearestEnemyLTemp = null, nearestEnemyRTemp = null;
        float minDistanceL = -Constants.WORLD_WIDTH;
        float minDistanceR = Constants.WORLD_WIDTH;

        for (Enemy enemy : enemies){
            float diff = enemy.getPosition().x - hero.getPosition().x;
            if (diff < 0 && diff >= minDistanceL){
                minDistanceL = diff;
                nearestEnemyLTemp = enemy;
            }else if (diff >=0 && diff <= minDistanceR){
                minDistanceR = diff;
                nearestEnemyRTemp = enemy;
            }
        }

        if(nearestEnemyLTemp != null) nearestEnemies.add(nearestEnemyLTemp);
        if(nearestEnemyRTemp != null) nearestEnemies.add(nearestEnemyRTemp);

        return nearestEnemies;
    }



    public ArenaScreen getArenaScreen() {
        return arenaScreen;
    }

    public void incKillCount(){
        killCount++;
        if (killCount % 5 == 0) hero.setRovenades(hero.getRovenades() + 2);
    }

    private void spawnEnemy(){
        float spawnX = MathUtils.randomBoolean() ? Constants.WORLD_WIDTH / 2f : - Constants.WORLD_WIDTH / 2f;
        Enemy enemy = new Enemy(world, spawnX, -3f);
        enemy.setDeathListener(worldListener);
        enemies.add(enemy);
    }

    @Override
    public boolean keyDown(int keycode) {
        if ( keycode == Input.Keys.SPACE) startNewRound();
        return super.keyDown(keycode);
    }

    private void startNewRound() {
        killCount = 0;
        world.destroyBody(hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.BODY).getBody());
        world.destroyBody(hero.getHeroPhysics().getFixture(HeroPhysics.FixtureNames.WEAPON).getBody());
        Hero hero = new Hero(arenaScreen.getWorld(), 0, -3f);
        Gdx.input.setInputProcessor(hero.getInputProcessor());
        hero.setWorldListener(worldListener);
        arenaScreen.setHero(hero);
        setGameState(GameState.RUNNING) ;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Array<Enemy> getEnemies() {
        return enemies;
    }
}
