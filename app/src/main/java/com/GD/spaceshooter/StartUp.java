/**
 * This is Team09's final project for the University of Oklahoma, 2024 Computer graphics class.
 * Authors: Jacob Maslovskiy and Zain Chaudhry
 * Version: 1.0
 *
 * This class serves as the startup screen of the game, which is the first screen shown to the user.
 * It provides a basic introduction to the application and transitions to the main game activity.
 */

package com.GD.spaceshooter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartUp extends AppCompatActivity {
    @Override
    // onCreate is called when the activity is first created.
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets the content view of the activity according to the layout defined in R.layout.startup.
        setContentView(R.layout.startup);
    }

    // This method is called when the user presses the button to start the game.
    public void startGame(View view) {
        // Creates an Intent to start the MainActivity, which is the main part of the game.
        startActivity(new Intent(this, MainActivity.class));
        // finish() is called to remove this activity from the back stack, so pressing back from the MainActivity won't return to this screen.
        finish();
    }
}
