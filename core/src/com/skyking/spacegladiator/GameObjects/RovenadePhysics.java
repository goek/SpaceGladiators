package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by Gök on 13.01.2016.
 */
public class RovenadePhysics {
    private Rovenade rovenade;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private BodyDef bodyDef;

    private PolygonShape polygonShape;

    private Body body;
    public RovenadePhysics(Rovenade rovenade){
        this.rovenade = rovenade;

        initBodyDef();
        initFixtureDef();
        initBody();

        disposeShape();
    }


    private void initBodyDef() {
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(rovenade.getPosition().x, rovenade.getPosition().y);
    }

    private void initFixtureDef() {
        polygonShape = new PolygonShape();
        polygonShape.setAsBox(rovenade.getWidth() / 2f, rovenade.getHeight() / 2f);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor = true;
    }

    private void initBody() {
        body = rovenade.getWorld().createBody(bodyDef);
        fixture = body.createFixture(fixtureDef);
    }

    private void disposeShape() {
        polygonShape.dispose();
    }

    public void update() {

    }

    public void setLinearVelocity(float x, float y) {
        body.setLinearVelocity(x,y);
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Fixture getFixture() {
        return fixture;
    }
}
