package com.skyking.spacegladiator;

import com.skyking.spacegladiator.GameObjects.Enemy;
import com.skyking.spacegladiator.GameObjects.Rovenade;
import com.skyking.spacegladiator.screens.ArenaLogic;
import com.skyking.spacegladiator.screens.ArenaScreen;

/**
 * Created by Gök on 17.01.2016.
 */
public class WorldListener {
    private ArenaLogic arenaLogic;
    private ArenaScreen arenaScreen;

    public WorldListener(ArenaLogic arenaLogic){
        this.arenaLogic = arenaLogic;
        this.arenaScreen = arenaLogic.getArenaScreen();
    }

    public void notifyEnemyDeath(Enemy enemy){
        arenaLogic.getEnemies().removeValue(enemy, false);
        arenaLogic.incKillCount();
    }

    public void notifyRovenadeSpawn(Rovenade rovenade){
        rovenade.setWorldListener(this);
        arenaLogic.getRovenades().add(rovenade);
    };

    public void notifyRovenadeDeath(Rovenade rovenade){
        arenaLogic.getRovenades().removeValue(rovenade, false);
    }

    public void notifyHeroDeath() {
        arenaScreen.pause();
        arenaLogic.stopGame();
    }
}
