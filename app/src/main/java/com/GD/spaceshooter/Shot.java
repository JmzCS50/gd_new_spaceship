/**
 * This is Team09's final project for the University of Oklahoma, 2024 Computer graphics class.
 * Authors: Jacob Maslovskiy and Zain Chaudhry
 * Version: 1.0*
 *
 * Represents an enemy rock in the Space Shooter game.
 * This class encapsulates the properties and functionalities of a rock,
 * including its bitmap representation, position, and dimensions.
 */

package com.GD.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Shot {
    Bitmap shot;
    Context context;
    int shx, shy;

    public Shot(Context context, int shx, int shy) {
        this.context = context;
        shot = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.shot);
        this.shx = shx;
        this.shy = shy;
    }
    public Bitmap getShot(){
        return shot;
    }
    public int getShotWidth() {
        return shot.getWidth();
    }
    public int getShotHeight() {
        return shot.getHeight();
    }
}
