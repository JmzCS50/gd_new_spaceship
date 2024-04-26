/**
 * This is Team09's final project for the University of Oklahoma, 2024 Computer Graphics class.
 * Authors: Jacob Maslovskiy and Zain Chaudhry
 * Version: 1.0
 *
 * The SpaceShooter class provides a space shooter game view where players control a spaceship
 * to dodge and destroy incoming enemy ships and rocks. This class manages game elements like
 * spaceship movement, firing, enemy generation, and collision detection.
 */
package com.GD.spaceshooter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SpaceShooter(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}