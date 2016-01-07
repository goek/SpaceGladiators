package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.util.AnimationManager;

/**
 * Created by Gök on 05.01.2016.
 */
public class HeroDrawer {
    private Hero hero;
    private AnimationManager<Animations> animationManager;
    private Vector2 idlePosition, runningPosition, attackPosition;
    private float runningWidth, runningHeight, attackWidth, attackHeight;
    public enum Animations{IDLE, RUNNING, ATTACK};

    public HeroDrawer(Hero hero){
        this.hero = hero;
        animationManager = new AnimationManager<Animations>();
        initAnimations();
    }

    public void draw(SpriteBatch batch){
        switch (hero.getState()) {
            case IDLE:
                animationManager.play(Animations.IDLE, batch, idlePosition, hero.getWidth(), hero.getHeight()
                        , (hero.getOrientation() == Hero.Orientation.RIGHT));
                break;
            case RUNNING:
                animationManager.play(Animations.RUNNING, batch, runningPosition, runningWidth,  runningHeight
                        , (hero.getOrientation() == Hero.Orientation.RIGHT));
                break;
            case ATTACK:
                animationManager.play(Animations.IDLE, batch, idlePosition, hero.getWidth(), hero.getHeight()
                        , (hero.getOrientation() == Hero.Orientation.RIGHT));
                break;
        }
    }

    private void initAnimations() {
        float ratioW = Assets.destIdleAtlas.getRegions().first().getRotatedPackedWidth() / Hero.HeroConstants.WIDTH;

        idlePosition = new Vector2(hero.getPosition().x - hero.getWidth() / 2,
                                     hero.getPosition().y - hero.getHeight() / 2);

        runningWidth = Assets.destRunAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        runningHeight = Assets.destRunAtlas.getRegions().first().getRotatedPackedHeight() / ratioW;

        runningPosition = new Vector2(hero.getPosition().x - runningWidth/2,
                                        hero.getPosition().y - runningHeight/2);

        animationManager.add(Animations.IDLE, new Animation(1/60f, Assets.destIdleAtlas.getRegions()));
        animationManager.add(Animations.RUNNING, new Animation(1/50f, Assets.destRunAtlas.getRegions()));
    }


    public void update(){
        idlePosition.set(hero.getPosition().x - hero.getWidth() / 2,
                hero.getPosition().y - hero.getHeight() / 2);

        runningPosition.set(hero.getPosition().x - runningWidth/2,
                                hero.getPosition().y - runningHeight/2);
    }



}
