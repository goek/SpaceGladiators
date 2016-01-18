package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Gök on 15.01.2016.
 */
public class EnemyBrain {
    private Enemy enemy;
    private float time = 0;
    private float currentWaitTime = 0;
    private Vector2 heroPosition;

    private enum AiState{WAITING, LOOKINGFORHERO, APPROACHINGHERO, CANATTACK, ATTACKING }

    private AiState aiState = AiState.LOOKINGFORHERO;
    private AiState afterWaitState = null;

    private boolean hasDirectSight = false; // is going to be set from outside
    private boolean isAttacking = false;
    private int attackCounter = 0;
    private int hitsPerSequence;

    public EnemyBrain(Enemy enemy){
        this.enemy = enemy;
    }

    public void update(){
        switch (aiState) {
            case LOOKINGFORHERO:
                checkDirectSight();
                break;
            case APPROACHINGHERO:
                checkAttackingRange();
                break;
            case CANATTACK:
                startAttackingSequence();
                break;
            case ATTACKING:
                attack();
                break;
            case WAITING:
                checkWaitOver();
                break;
        }
    }

    // direct AI functions
    private void checkAttackingRange() {
        if (Math.abs(enemy.getPosition().x - heroPosition.x)
                < enemy.getEnemyPhysics().WEAPONRANGE + Hero.Constants.WIDTH * 0.9f){
            setAiState(AiState.CANATTACK);
        } else {
            setAiState(AiState.LOOKINGFORHERO);
        }
    }

    private void startAttackingSequence() {
        enemy.stop();
        hitsPerSequence = MathUtils.random(2,4);
        waitAndSetAiState(0.2f, AiState.ATTACKING);
    }

    private void attack(){
        attackCounter++;
        enemy.punch();
        waitAndSetAiState(MathUtils.random(0.4f, 0.7f), AiState.ATTACKING);
        if(attackCounter == hitsPerSequence){
            attackCounter = 0;
            checkAttackingRange();
        }
    }

    private void checkDirectSight() {
        if(hasDirectSight){
            enemy.run(getRunOrientation(true));
            setAiState(AiState.APPROACHINGHERO);
        }else setAiState(AiState.LOOKINGFORHERO);
    }

    public void setHasDirectSight(boolean hasDirectSight) {
        this.hasDirectSight = hasDirectSight;
    }

    public void delegateHeroPosition(Vector2 heroPosition){
        this.heroPosition = heroPosition;
    }

    // Helper functions

    /*
        can be called in any function, if a delay is wanted
        After the time delay the brainUpdate is resumed with the wanted state
     */
    private void waitAndSetAiState(float waitTime, AiState state){
        currentWaitTime = waitTime;
        afterWaitState = state;
        setAiState(AiState.WAITING);

    }

    /*
        Returns true if the queried time has passed.
     */
    private boolean timePassed(float timeQuery){
        time += Gdx.graphics.getDeltaTime();
        if(timeQuery <= time){
            time = 0;
            return true;
        }else{
            return false;
        }
    }

    /*
        gets the orientation in dependence of the hero position and desired behaviour
     */
    private Enemy.Orientation getRunOrientation(boolean towardsHero){
        if(enemy.getPosition().x < heroPosition.x){
            return towardsHero ? Enemy.Orientation.RIGHT : Enemy.Orientation.LEFT;
        } else {
            return towardsHero ? Enemy.Orientation.LEFT : Enemy.Orientation.RIGHT;
        }
    }

    /*
        is called in the waiting state, this function can be seen as the second part of the
        waitAndSetState function
     */
    private void checkWaitOver() {
        if(timePassed(currentWaitTime)) setAiState(afterWaitState);
    }

    public void setAiState(AiState aiState) {
        this.aiState = aiState;
    }
}

