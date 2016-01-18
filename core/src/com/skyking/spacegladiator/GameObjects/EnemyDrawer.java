package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.screens.ArenaScreen;
import com.skyking.spacegladiator.util.AnimationManager;
import com.skyking.spacegladiator.util.Tools;

/**
 * Created by Gök on 07.01.2016.
 */
public class EnemyDrawer {
    private Enemy enemy;
    private AnimationManager<Animations> animationManager;
    private Vector2 idlePosition, runningPosition, attackPosition;
    private float runningWidth, runningHeight, attackWidth, attackHeight;
    public enum Animations{IDLE, RUNNING, ATTACK, DIE};

    private Sprite healthPoint1, healthPoint2, healthPoint3;

    public EnemyDrawer(Enemy enemy){
        this.enemy = enemy;
        animationManager = new AnimationManager<Animations>();
        initAnimations();
    }


    public void draw(SpriteBatch batch){
        switch (enemy.getState()) {
            case IDLE:
                animationManager.play(Animations.IDLE, batch, idlePosition, enemy.getWidth(), enemy.getHeight()
                        , (enemy.getOrientation() == Enemy.Orientation.RIGHT), true);
                break;
            case RUNNING:
                animationManager.play(Animations.RUNNING, batch, runningPosition, runningWidth,  runningHeight
                        , (enemy.getOrientation() == Enemy.Orientation.RIGHT), true);
                break;
            case ATTACK:
                animationManager.play(Animations.ATTACK, batch, attackPosition, attackWidth, attackHeight
                        , (enemy.getOrientation() == Enemy.Orientation.RIGHT), false);
                break;
            case DIE:
                boolean isFinished =
                animationManager.play(Animations.DIE, batch, idlePosition, enemy.getWidth(), enemy.getHeight()
                        , (enemy.getOrientation() == Enemy.Orientation.RIGHT), false);

                if (isFinished) enemy.deadAndBury();
                break;

        }

        switch (enemy.getHealthPoints()){
            case 1:
                healthPoint1.draw(Tools.batch);
                break;
            case 2:
                healthPoint1.draw(Tools.batch);
                healthPoint2.draw(Tools.batch);
                break;
            case 3:
                healthPoint1.draw(Tools.batch);
                healthPoint2.draw(Tools.batch);
                healthPoint3.draw(Tools.batch);
                break;
            default:
                break;
        }
    }

    private void initAnimations() {
        float ratioW = Assets.destIdleAtlas.getRegions().first().getRotatedPackedWidth() / Hero.Constants.WIDTH;

        idlePosition = new Vector2(0, 0);

        runningWidth = Assets.destRunAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        runningHeight = Assets.destRunAtlas.getRegions().first().getRotatedPackedHeight() / ratioW;
        runningPosition = new Vector2(0, 0);

        attackWidth = Assets.destPunchAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        attackHeight = Assets.destPunchAtlas.getRegions().first().getRotatedPackedHeight() / ratioW;
        attackPosition = new Vector2(0, 0);

        animationManager.add(Animations.IDLE, new Animation(1/60f, Assets.enemyIdleAtlas.getRegions()));
        animationManager.add(Animations.RUNNING, new Animation(1/50f, Assets.enemyRunAtlas.getRegions()));
        animationManager.add(Animations.ATTACK, new Animation(1 / 70f, Assets.enemyPunchAtlas.getRegions()));
        animationManager.add(Animations.DIE, new Animation(1/30f, Assets.enemyDieAtlas.getRegions()));

        healthPoint1 = new Sprite(Assets.healthPointTexture);
        healthPoint2 = new Sprite(Assets.healthPointTexture);
        healthPoint3 = new Sprite(Assets.healthPointTexture);

        healthPoint1.setSize(healthPoint1.getWidth() / ArenaScreen.SCALE * 2f
                           , healthPoint2.getHeight() / ArenaScreen.SCALE * 2f);

        healthPoint2.setSize(healthPoint1.getWidth(), healthPoint1.getHeight());
        healthPoint3.setSize(healthPoint1.getWidth(), healthPoint1.getHeight());

    }


    public void update(){
        idlePosition.set(enemy.getPosition().x - enemy.getWidth() / 2f,
                         enemy.getPosition().y - enemy.getHeight() / 2f);

        runningPosition.set(enemy.getPosition().x - runningWidth / 2f,
                            enemy.getPosition().y - runningHeight / 2f);

        float attackDisplacementX = (enemy.getOrientation() == Enemy.Orientation.RIGHT) ?
                                        attackWidth * 0.23f : -attackWidth * 0.23f;

        attackPosition.set(enemy.getPosition().x - attackWidth / 2f  + attackDisplacementX,
                           enemy.getPosition().y - attackHeight / 2f);

        healthPoint1.setPosition(enemy.getPosition().x - (healthPoint1.getWidth() + enemy.getWidth()) / 3f,
                                 enemy.getPosition().y + (healthPoint1.getHeight() + enemy.getHeight() / 2f));

        healthPoint2.setPosition(enemy.getPosition().x - healthPoint2.getWidth() / 3f,
                                 enemy.getPosition().y + (healthPoint1.getHeight() + enemy.getHeight() / 2f));

        healthPoint3.setPosition(enemy.getPosition().x - (healthPoint1.getWidth() - enemy.getWidth()) / 3f,
                                 enemy.getPosition().y + (healthPoint1.getHeight() + enemy.getHeight() / 2f));
    }


}
