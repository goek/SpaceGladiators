package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
        float ratioW = Assets.destIdleAtlas.getRegions().first().getRotatedPackedWidth() / Hero.Constants.WIDTH;

        idlePosition = new Vector2(0, 0);

        runningWidth = Assets.destRunAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        runningHeight = Assets.destRunAtlas.getRegions().first().getRotatedPackedHeight() / ratioW;
        runningPosition = new Vector2(0, 0);

        attackWidth = Assets.destPunchAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        attackHeight = Assets.destPunchAtlas.getRegions().first().getRotatedPackedHeight() / ratioW;
        attackPosition = new Vector2(0, 0);

        animationManager.add(Animations.IDLE, new Animation(1/60f, Assets.destIdleAtlas.getRegions()));
        animationManager.add(Animations.RUNNING, new Animation(1 / 50f, Assets.destRunAtlas.getRegions()));
        animationManager.add(Animations.ATTACK, new Animation(1 / 70f, Assets.destPunchAtlas.getRegions()));
    }


    public void update(){
        idlePosition.set(hero.getPosition().x - hero.getWidth() / 2f,
                         hero.getPosition().y - hero.getHeight() / 2f);

        runningPosition.set(hero.getPosition().x - runningWidth / 2f,
                            hero.getPosition().y - runningHeight / 2f);

        float attackDisplacementX = (hero.getOrientation() == Hero.Orientation.RIGHT) ?
                                     attackWidth * 0.23f : -attackWidth * 0.23f;

        attackPosition.set(hero.getPosition().x - attackWidth / 2f + attackDisplacementX,
                hero.getPosition().y - attackHeight / 2f);

        HealthBar.getInstance().delegateHeroHealth(hero.getHealth());
    }



    public static class HealthBar extends Actor{
        private Sprite healthBarSprite = new Sprite(Assets.destHealthBar);
        private Sprite healthSprite = new Sprite(Assets.destHealth);

        private final float SCALE = 0.25f;
        private final float aspectRatio = Assets.destHealthBar.getWidth() / Assets.destHealthBar.getHeight();
        private final float WIDTH = Gdx.graphics.getWidth() * SCALE;
        private final float HEIGHT= WIDTH / aspectRatio;

        private final float x = (Gdx.graphics.getWidth() - WIDTH) * 0.5f;
        private final float y = (Gdx.graphics.getHeight() * 0.9f);

        private static final HealthBar healthBar = new HealthBar();
        private int heroHealth = 0;

        private HealthBar (){
            healthBarSprite.setBounds(x, y, WIDTH, HEIGHT);
            healthSprite.setPosition(x + (HEIGHT * 0.25f), y + (HEIGHT * 0.25f));
        }

        public static HealthBar getInstance(){
            return healthBar;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            healthBarSprite.draw(batch);
            healthSprite.draw(batch);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            healthSprite.setSize((WIDTH - HEIGHT * 0.5f) * heroHealth / Hero.MAXHEALTH, HEIGHT * 0.5f);
        }

        public void delegateHeroHealth(int heroHealth){
            this.heroHealth = heroHealth;
        }
    }
}
