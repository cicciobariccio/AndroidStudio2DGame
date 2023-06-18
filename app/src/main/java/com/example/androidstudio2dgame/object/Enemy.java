package com.example.androidstudio2dgame.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgame.GameLoop;
import com.example.androidstudio2dgame.R;

/**
 * Enemy is a character which always moves in the direction of the player.
 * The Enemy class is an extension of a Circle, which is an extension of GameObject
 */

public class Enemy extends Circle {


    private static final double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECONDS = SPAWNS_PER_MINUTE / 60;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS / SPAWNS_PER_SECONDS;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    private final Player player;

    public Enemy(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
    }

    public Enemy(Context context, Player player) {
        super(
                context,
                ContextCompat.getColor(context, R.color.enemy),
                Math.random() * 1000,
                Math.random() * 1000,
                30);
        this.player = player;
    }

    /**
     * readyToSpawn check if a new enemy should spawn, according to the decided number of spawns per minute (SPAWNS_PER_MINUTE)
     * @return
     */
    public static boolean readyToSpawn() {
        if (updatesUntilNextSpawn <= 0) {
            updatesUntilNextSpawn += UPDATES_PER_SPAWN;
            return true;
        } else {
            updatesUntilNextSpawn --;
            return false; 
        }
    }

    @Override
    public void update() {
        // =========================================================================================
        // Update velocity of the enemy so that the velocity is in the direction of the player
        // =========================================================================================
        // Calculate vector from enemy to player (in x and y)
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        // Calculate absolute distance between enemy (this) to player
        double distanceToPlayer = GameObject.getDistanceBetweenToObjects(this, player);

        // Calculate direction from enemy to player
        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;

        // Set velocity in the direction to the player
        if (distanceToPlayer > 0){
            velocityX = directionX * MAX_SPEED;
            velocityY = directionY * MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }

        // Update the position of enemy
        positionX += velocityX;
        positionY += velocityY;
    }
}
