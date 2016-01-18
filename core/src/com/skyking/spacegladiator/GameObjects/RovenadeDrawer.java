package com.skyking.spacegladiator.GameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.skyking.spacegladiator.Assets;
import com.skyking.spacegladiator.util.AnimationManager;

/**
 * Created by Gök on 13.01.2016.
 */
public class RovenadeDrawer {
    private Rovenade rovenade;

    private enum Animations{SPAWN, DRIVE, ONCONTACT;}
    private float spawnWidth, spawnHeight, driveWidth, driveHeight, onContactWidth, onContactHeight;
    private Vector2 spawnPos, drivePos, onContactPos;

    private AnimationManager<Animations> animationManager;


    public RovenadeDrawer(Rovenade rovenade){
        this.rovenade = rovenade;
        animationManager = new AnimationManager<Animations>();
        initAnimations();
    }

    public void draw(SpriteBatch batch) {
        switch (rovenade.getState()) {
            case SPAWN:
                boolean isFinished =
                animationManager.play(Animations.SPAWN, batch, spawnPos, spawnWidth, spawnHeight
                        , rovenade.getOrientation() == Rovenade.Orientation.RIGHT, false);
                if (isFinished) rovenade.drive(rovenade.getOrientation());
                break;
            case DRIVE:
                animationManager.play(Animations.DRIVE, batch, drivePos, driveWidth, driveHeight
                        , rovenade.getOrientation() == Rovenade.Orientation.RIGHT, true);
                break;
            case ONCONTACT:
                boolean isOnContactFinished =
                animationManager.play(Animations.ONCONTACT, batch, onContactPos, onContactWidth, onContactHeight,
                        rovenade.getOrientation() == Rovenade.Orientation.RIGHT, false);
                if (isOnContactFinished) rovenade.deadAndBury();
                break;
        }
    }

    private void initAnimations() {
        float ratioW = Assets.rovenadeSpawnAtlas.getRegions().first().getRotatedPackedWidth() / Rovenade.Constants.WIDTH;

        spawnWidth = Rovenade.Constants.WIDTH;
        spawnHeight = Rovenade.Constants.HEIGHT;

        driveWidth = Assets.rovenadeDriveAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        driveHeight = Assets.rovenadeDriveAtlas.getRegions().first().getRotatedPackedHeight()/ ratioW;

        onContactWidth = Assets.rovenadeOnContactAtlas.getRegions().first().getRotatedPackedWidth() / ratioW;
        onContactHeight = Assets.rovenadeOnContactAtlas.getRegions().first().getRotatedPackedHeight()/ ratioW;

        animationManager.add(Animations.SPAWN, new Animation(1/200f, Assets.rovenadeSpawnAtlas.getRegions()));
        animationManager.add(Animations.DRIVE, new Animation(1/60f, Assets.rovenadeDriveAtlas.getRegions()));
        animationManager.add(Animations.ONCONTACT, new Animation(1/30f, Assets.rovenadeOnContactAtlas.getRegions()));
    }

    public void update() {
        spawnPos = new Vector2(rovenade.getPosition().x - spawnWidth * 0.5f
                            ,  rovenade.getPosition().y - spawnHeight * 0.5f );

        drivePos = new Vector2(rovenade.getPosition().x - driveWidth * 0.5f
                           ,   rovenade.getPosition().y - driveHeight * 0.5f);

        onContactPos = new Vector2(rovenade.getPosition().x - onContactWidth * 0.5f
                                ,  rovenade.getPosition().y - onContactHeight * 0.5f);

    }
}
