package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.skyking.spacegladiator.util.AnimationManager;

/**
 * Created by Gök on 07.01.2016.
 */
public class EnemyDrawer {
    private Enemy enemy;
    private AnimationManager<Animations> animationManager;
    private Vector2 idlePosition, runningPosition, attackPosition;
    private float runningWidth, runningHeight, attackWidth, attackHeight;
    public enum Animations{IDLE, RUNNING, ATTACK};

    public EnemyDrawer(Enemy enemy){
        this.enemy = enemy;
        animationManager = new AnimationManager<Animations>();
        initAnimations();
    }

    private void initAnimations() {

    }
}
