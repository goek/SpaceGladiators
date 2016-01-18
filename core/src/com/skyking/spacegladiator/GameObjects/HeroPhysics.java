package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Gök on 05.01.2016.
 */
public class HeroPhysics {
    private Hero hero;
    private Body playerBody, weaponBody;
    private BodyDef playerBodyDef, weaponBodyDef;
    private FixtureDef feetFixtureDef, bodyFixtureDef, weaponFixtureDef;
    private ObjectMap<FixtureNames, Fixture> fixtureMap;



    public enum FixtureNames{FEET, BODY, WEAPON;};
    private PolygonShape playerBodyShape, weaponShape;
    private CircleShape feetShape;
    private float adjustWidthPercentage = 0.8f;


    public enum Action{IDLE, ATTACK;};
    private Action currentAction = Action.IDLE;
    boolean isDescending = false;

    public HeroPhysics(Hero hero){
        this.hero = hero;

        initBodyDef();
        initFixtureDef();
        initBodys();

        disposeShapes();
    }

    private void initBodyDef() {
        playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(hero.getPosition());

        weaponBodyDef = new BodyDef();
        weaponBodyDef.type = BodyDef.BodyType.DynamicBody;
        weaponBodyDef.position.set(hero.getPosition().x, hero.getPosition().y + hero.getHeight() / 6f);
    }

    private void initFixtureDef() {
        fixtureMap = new ObjectMap<FixtureNames, Fixture>();

        // FixtureDef for Feet
        float feetRadius = hero.getWidth()/2 * adjustWidthPercentage;
        Vector2 feetDisplacement = new Vector2(0, - ((hero.getHeight() / 2) - feetRadius));
        feetShape = new CircleShape();
        feetShape.setRadius(feetRadius);
        feetShape.setPosition(feetDisplacement);
        feetFixtureDef = new FixtureDef();
        feetFixtureDef.shape = feetShape;
        feetFixtureDef.isSensor = true;

        // Fixture for Body
        Vector2 bodyDisplacement = new Vector2(0, feetRadius / 2);
        playerBodyShape = new PolygonShape();
        playerBodyShape.setAsBox(hero.getWidth() / 2 * adjustWidthPercentage, (hero.getHeight() - feetRadius) / 2, bodyDisplacement, 0);
        bodyFixtureDef = new FixtureDef();
        bodyFixtureDef.shape = playerBodyShape;
        bodyFixtureDef.isSensor = true;

        // Fixture for Weapon/Fist etc.
        Vector2 weaponDisplacement = new Vector2(0, hero.getHeight() / 25);
        weaponShape = new PolygonShape();
        weaponShape.setAsBox(hero.getWidth() / 2 * adjustWidthPercentage * 0.5f, hero.getHeight() / 16, weaponDisplacement, 0);
        weaponFixtureDef = new FixtureDef();
        weaponFixtureDef.shape = weaponShape;
        weaponFixtureDef.isSensor = true;
    }

    private void initBodys() {
        playerBody = hero.getWorld().createBody(playerBodyDef);
        Fixture bodyFixture = playerBody.createFixture(bodyFixtureDef);
        Fixture feetFixture = playerBody.createFixture(feetFixtureDef);

        weaponBody = hero.getWorld().createBody(weaponBodyDef);
        Fixture weaponFixture = weaponBody.createFixture(weaponFixtureDef);

        fixtureMap.put(FixtureNames.BODY, bodyFixture);
        fixtureMap.put(FixtureNames.FEET, feetFixture);
        fixtureMap.put(FixtureNames.WEAPON, weaponFixture);


    }

    public void update() {

        switch (hero.getState()) {
            case ATTACK:
                if (Math.abs(weaponBody.getPosition().x - hero.getPosition().x) >= hero.getWidth() *1.1f){
                    switch (hero.getOrientation()) {
                        case LEFT:
                            weaponBody.setLinearVelocity(Hero.Constants.PUNCHVELOCITY, 0);
                            break;
                        case RIGHT:
                            weaponBody.setLinearVelocity( - Hero.Constants.PUNCHVELOCITY, 0);
                            break;
                    }
                }

                if (Math.abs(weaponBody.getPosition().x - hero.getPosition().x) < 0.0001f){
                    weaponBody.setLinearVelocity(0, 0);
                    hero.setState(Hero.State.IDLE);             //Finishing attack, then go back to idle
                }
                break;
            case JUMP:
                if(!isDescending) {
                    //applyForce(hero.jumpVelocity, Hero.HeroConstants.JUMPVELOCITY);
                    Gdx.app.log("VELOCITY", String.valueOf(hero.jumpVelocity));
                }
                if (hero.getPosition().y >= hero.getHeight()){
                    applyForce(hero.jumpVelocity, -Hero.Constants.JUMPVELOCITY);
                    isDescending = true;
                }
                if (Math.abs(hero.getPosition().y - -3f) < 0.0001){
                    hero.stop();
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
        if(hero.getState() == Hero.State.ATTACK) return;

        playerBody.setLinearVelocity(x, y);
        weaponBody.setLinearVelocity(x, y);

    }

    public void setPosition(Vector2 position) {
        playerBody.getPosition().set(position);
        weaponBody.getPosition().set(hero.getPosition().x, hero.getPosition().y + hero.getHeight() / 6f);
    }

    public void applyForce(float x, float y){
        playerBody.applyForceToCenter(x, y, false);
        weaponBody.applyForceToCenter(x, y, false);
    }

    public void useWeapon(){
        if(hero.getState() == Hero.State.ATTACK) return;
        switch (hero.getOrientation()) {
            case LEFT:
                weaponBody.setLinearVelocity( - Hero.Constants.PUNCHVELOCITY, 0);
                break;
            case RIGHT:
                weaponBody.setLinearVelocity(Hero.Constants.PUNCHVELOCITY, 0);
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
