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
                        , (hero.getOrientation() == Hero.Orientation.RIGHT), true);
                break;
            case RUNNING:
                animationManager.play(Animations.RUNNING, batch, runningPosition, runningWidth,  runningHeight
                        , (hero.getOrientation() == Hero.Orientation.RIGHT), true);
                break;
            case ATTACK:
                animationManager.play(Animations.ATTACK, batch, attackPosition, attackWidth, attackHeight
                        , (hero.getOrientation() == Hero.Orientation.RIGHT), false);
                break;
        }
    }

    private void initAnimations() {
        float ratioW = Assets.destIdleAtlas.getRegions().first().getRotatedPackedWidth() / Hero.HeroConstants.WIDTH;

        idlePosition = new Vector2(0, 0);

        runningWidth = Assets.destRunAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        runningHeight = Assets.destRunAtlas.getRegions().first().getRotatedPackedHeight() / ratioW;
        runningPosition = new Vector2(0, 0);

        attackWidth = Assets.destPunchAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        attackHeight = Assets.destPunchAtlas.getRegions().first().getRotatedPackedHeight() / ratioW;
        attackPosition = new Vector2(0, 0);

        animationManager.add(Animations.IDLE, new Animation(1/60f, Assets.destIdleAtlas.getRegions()));
        animationManager.add(Animations.RUNNING, new Animation(1/65f, Assets.destRunAtlas.getRegions()));
        animationManager.add(Animations.ATTACK, new Animation(1/50f, Assets.destPunchAtlas.getRegions()));
    }


    public void update(){
        idlePosition.set(hero.getPosition().x - hero.getWidth() / 2,
                hero.getPosition().y - hero.getHeight() / 2);

        runningPosition.set(hero.getPosition().x - runningWidth / 2,
                                hero.getPosition().y - runningHeight / 2);

        float attacDisplacementX = ( hero.getOrientation() == Hero.Orientation.RIGHT ) ?
                                                        attackWidth * 0.23f : -attackWidth * 0.23f;

        attackPosition.set(hero.getPosition().x - attackWidth / 2  + attacDisplacementX
                         , hero.getPosition().y - attackHeight / 2);
    }



}
