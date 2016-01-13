package com.skyking.spacegladiator.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * An animation manager class mainly to reduce code in the draw methods.
 * Also useful functions like flipAnimation
 * Created by Gök on 04.01.2016.
 */
public class AnimationManager<Key> {
    private ObjectMap<Key, Animation> animationMap;
    private float time = 0;

    public AnimationManager(){
        animationMap = new ObjectMap<Key, Animation>();
    }

    public void add(Key key, Animation animation){
        animationMap.put(key, animation);
    }

    public Animation getAnimation(Key key){
        return animationMap.get(key);
    }
    boolean isPlayStart = true;
    private Key currentKey;

    //has to be called in the render method with spriteBatch passed on
    public void play(Key key, SpriteBatch batch, Vector2 position, float width, float height){

        time += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animationMap.get(key).getKeyFrame(time,false);
        batch.draw(currentFrame, position.x, position.y, width, height);
        if (animationMap.get(key).isAnimationFinished(time)) resetTime();

    }

    public void play(Key key, SpriteBatch batch, Vector2 position, float width, float height, boolean mirrorX, boolean loop){
        if (isPlayStart) {
            currentKey = key;
            isPlayStart = false;
        }

        if (currentKey != key) {
            isPlayStart = true;
            resetTime();
        }

        time += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = animationMap.get(key).getKeyFrame(time,false);
        if(mirrorX && currentFrame.isFlipX()==false) currentFrame.flip(true, false);
        if(!mirrorX && currentFrame.isFlipX()==true) currentFrame.flip(true, false);
        batch.draw(currentFrame, position.x, position.y, width, height);
        if (animationMap.get(key).isAnimationFinished(time) && loop) resetTime();

    }

    public void flipAnimation(Key key, boolean x, boolean y){
        TextureRegion[] textureRegions = animationMap.get(key).getKeyFrames();
        for (TextureRegion textureRegion : textureRegions){
            textureRegion.flip(x, y);
        }
    }

    private void resetTime(){
        time = 0;
    }
}
