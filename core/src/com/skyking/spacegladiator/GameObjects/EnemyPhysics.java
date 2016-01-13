package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Gök on 07.01.2016.
 */
public class EnemyPhysics {
    private Enemy enemy;
    private Body playerBody, weaponBody;
    private BodyDef playerBodyDef, weaponBodyDef;
    private FixtureDef feetFixtureDef, bodyFixtureDef, weaponFixtureDef;
    private ObjectMap<FixtureNames, Fixture> fixtureMap;
    public enum FixtureNames{FEET, BODY, WEAPON;};
    private PolygonShape playerBodyShape, weaponShape;
    private CircleShape feetShape;

    private float adjustWidthPercentage = 1;
    public enum Action{IDLE, ATTACK};
    private Action currentAction = Action.IDLE;

    public EnemyPhysics(Enemy enemy){
        this.enemy = enemy;

        initBodyDef();
        initFixtureDef();
        initBodys();

        disposeShapes();
    }

    private void initBodyDef() {
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(enemy.getPosition());

        weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;
        weaponBodyDef.position.set(enemy.getPosition().x, enemy.getPosition().y + enemy.getHeight() / 6f);
    }

    private void initFixtureDef() {
        fixtureMap = new ObjectMap<FixtureNames, Fixture>();

        float feetRadius = enemy.getWidth()/2 * adjustWidthPercentage;
        Vector2 feetDisplacement = new Vector2(0, - ((enemy.getHeight() / 2) - feetRadius));
        feetShape = new CircleShape();
        feetShape.setRadius(feetRadius);
        feetShape.setPosition(feetDisplacement);
        feetFixtureDef = new FixtureDef();
        feetFixtureDef.shape = feetShape;
        feetFixtureDef.isSensor = true;

        Vector2 bodyDisplacement = new Vector2(0, feetRadius / 2);
        playerBodyShape = new PolygonShape();
        playerBodyShape.setAsBox(enemy.getWidth() / 2 * adjustWidthPercentage, (enemy.getHeight() - feetRadius) / 2, bodyDisplacement, 0);
        bodyFixtureDef = new FixtureDef();
        bodyFixtureDef.shape = playerBodyShape;
        bodyFixtureDef.isSensor = true;

        Vector2 weaponDisplacement = new Vector2(0, enemy.getHeight() / 25);
        weaponShape = new PolygonShape();
        weaponShape.setAsBox(enemy.getWidth() / 2 * adjustWidthPercentage, enemy.getHeight() / 16, weaponDisplacement, 0);
        weaponFixtureDef = new FixtureDef();
        weaponFixtureDef.shape = weaponShape;
        weaponFixtureDef.isSensor = true;

    }

    private void initBodys() {
        playerBody = enemy.getWorld().createBody(playerBodyDef);
        Fixture bodyFixture = playerBody.createFixture(bodyFixtureDef);
        Fixture feetFixture = playerBody.createFixture(feetFixtureDef);

        weaponBody = enemy.getWorld().createBody(weaponBodyDef);
        Fixture weaponFixture = weaponBody.createFixture(weaponFixtureDef);

        fixtureMap.put(FixtureNames.BODY, bodyFixture);
        fixtureMap.put(FixtureNames.FEET, feetFixture);
        fixtureMap.put(FixtureNames.WEAPON, weaponFixture);
    }

    public void update() {

        switch (enemy.getState()) {
            case ATTACK:
                if (Math.abs(weaponBody.getPosition().x - enemy.getPosition().x) >= enemy.getWidth()){
                    switch (enemy.getOrientation()) {
                        case LEFT:
                            weaponBody.setLinearVelocity(Hero.HeroConstants.PUNCHVELOCITY, 0);
                            break;
                        case RIGHT:
                            weaponBody.setLinearVelocity( - Hero.HeroConstants.PUNCHVELOCITY, 0);
                            break;
                    }

                }
                if (Math.abs(weaponBody.getPosition().x - enemy.getPosition().x) < 0.0001){
                    weaponBody.setLinearVelocity(0, 0);

                    enemy.setState(Enemy.State.IDLE);             //Finishing attack, then go back to idle
                }
                break;
        }


    }

    private void disposeShapes() {
        playerBodyShape.dispose();
        feetShape.dispose();
        weaponShape.dispose();
    }

    public void setLinearVelocity(float x, float y){
        if(enemy.getState() == Enemy.State.ATTACK) return;

        playerBody.setLinearVelocity(x, y);
        weaponBody.setLinearVelocity(x, y);

    }

    public void useWeapon(){
        if(enemy.getState() == Enemy.State.ATTACK) return;
        switch (enemy.getOrientation()) {
            case LEFT:
                weaponBody.setLinearVelocity( - Hero.HeroConstants.PUNCHVELOCITY, 0);
                break;
            case RIGHT:
                weaponBody.setLinearVelocity(Hero.HeroConstants.PUNCHVELOCITY, 0);
                break;
        }

    }

    public Vector2 getPosition(){
        return playerBody.getPosition();
    }

    public Fixture getFixture(FixtureNames fixtureNames){
        return fixtureMap.get(fixtureNames);
    }

}
