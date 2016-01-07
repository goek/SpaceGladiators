package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

import java.util.EnumSet;
import java.util.Set;

/**
 * Abstract Entity Class covering the basic positioning and getter, setter implementations
 * Created by Gök on 04.01.2016.
 */
public abstract class Entity2D {

    private float width, height;
    private Vector2 position, origin;

    public Entity2D() {
        position = new Vector2(0, 0);
        origin = new Vector2(0, 0);
    }

    public abstract void draw(SpriteBatch batch) ;
    public abstract void update();

    // setters and getters
    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getOrigin() {
        return origin;
    }
    public float getHeight() {
        return height;
    }
    public float getWidth() {
        return width;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public void setPosition(float x, float y) { this.position.set(x, y); }
    public void setOrigin(Vector2 origin) {
        this.origin = origin;
    }
    public void setOrigin(float x, float y) { this.origin.set(x, y); }
    public void setSize (float width, float height) {
        setWidth(width);
        setHeight(height);
    }
    public void setWidth(float width) {
        this.width = width;
    }
    public void setHeight(float height) {
        this.height = height;
    }




}