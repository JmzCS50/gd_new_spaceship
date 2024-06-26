/**
 * This is Team09's final project for the University of Oklahoma, 2024 Computer Graphics class.
 * Authors: Jacob Maslovskiy and Zain Chaudhry
 * Version: 1.0
 *
 * This class serves as the enemy spaceship class, which is drops the rocks on the player.
 */
package com.GD.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



import java.util.Random;

public class EnemySpaceship {
    Context context;
    Bitmap enemySpaceship;
    int ex, ey;
    int enemyVelocity;
    Random random;
    private int rockDropCooldown; // Cooldown before this spaceship can drop another rock

    public EnemySpaceship(Context context) {
        this.context = context;
        enemySpaceship = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket2);
        random = new Random();
        ex = 200 + random.nextInt(400); // Starting X position
        ey = 0; // Starting Y position, assumed to be top of the screen
        enemyVelocity = 20; // Starting velocity
        resetRockDropCooldown(); // Initialize rock drop cooldown
    }

    public void updateVelocity(int newVelocity) {
        this.enemyVelocity = newVelocity;
    }

    public Bitmap getEnemySpaceship(){
        return enemySpaceship;
    }

    int getEnemySpaceshipWidth(){
        return enemySpaceship.getWidth();
    }

    int getEnemySpaceshipHeight(){
        return enemySpaceship.getHeight();
    }

    // Reset the cooldown for dropping rocks
    private void resetRockDropCooldown() {
        // Set the cooldown to a random value between 150 to 300 frames for example
        this.rockDropCooldown = random.nextInt(30) + 20;
    }

    // Check if the spaceship can drop a rock
    public boolean canDropRock() {
        if (rockDropCooldown <= 0) {
            resetRockDropCooldown();
            return true;
        }
        rockDropCooldown--;
        return false;
    }
}
