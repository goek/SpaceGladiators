package com.skyking.spacegladiator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Gök on 26.12.2015.
 */
public class Dest {

    private float width, height;
    private Vector2 pos;

    private Animation destRun, destIdle, destIdleToRun, destRunToIdle;
    private float time = 0;

    public enum State { IDLE, RUNNING, ATTACK, IDLETORUN, RUNTOIDLE, FINISHRUN}
    private State destState = State.IDLE;

    float centralizeConstant;
    public Dest (){
        pos = new Vector2();
        pos.set(0, 0);

        destRun = new Animation(1/60f, Assets.destRunAtlas.getRegions());
        destIdle = new Animation(1/45f, Assets.destIdleAtlas.getRegions());
        destIdleToRun = new Animation(1/60f, Assets.destIdleToRunAtlas.getRegions());
        destRunToIdle = new Animation(1/60f, Assets.destRunToIdleAtlas.getRegions());

        centralizeConstant = destRun.getKeyFrame(0).getRegionWidth()/2 - destIdle.getKeyFrame(0).getRegionWidth() / 2 ;
    }

    public void draw(SpriteBatch batch){
        switch(destState){
            case IDLE:
                batch.draw(destIdle.getKeyFrame(time, true), pos.x + centralizeConstant, pos.y);
                break;
            case IDLETORUN:
                batch.draw(destIdleToRun.getKeyFrame(time, false), pos.x, pos.y);
                if(destIdleToRun.isAnimationFinished(time)){
                    setState(State.RUNNING);
                    resetTime();
                }
                break;
            case RUNNING:
                batch.draw(destRun.getKeyFrame(time, false), pos.x, pos.y);
                if(destRun.isAnimationFinished(time)) resetTime();
                break;
            case FINISHRUN:
                if(!destRun.isAnimationFinished(time)){
                batch.draw(destRun.getKeyFrame(time, true), pos.x, pos.y);
                }else{
                    batch.draw(destRun.getKeyFrame(time, false), pos.x, pos.y);          //avoids flickering -> Drawing last Frame for last time
                    setState(State.RUNTOIDLE);
                    resetTime();
                }
                break;
            case RUNTOIDLE:
                batch.draw(destRunToIdle.getKeyFrame(time, false), pos.x, pos.y);
                    if (destRunToIdle.isAnimationFinished(time)) {
                        setState(State.IDLE);
                        resetTime();
                    }
                break;
            case ATTACK:
                break;
        }
        time += Gdx.graphics.getDeltaTime();

    }

    public void setState(State state){
        destState = state;
    }

    public Dest.State getState(){
        return this.destState;
    }

    public void setPos(float x, float y){
        pos.set(x, y);
    }

    public void resetTime(){
        time = 0;
    }


}
