/**
 * This is Team09's final project for the University of Oklahoma, 2024 Computer Graphics class.
 * Authors: Jacob Maslovskiy and Zain Chaudhry
 * Version: 1.0
 *
 *  Represents a shot fired from the spaceship in the Space Shooter game.
 *  This class encapsulates the properties and functionalities of a shot,
 *  such as its bitmap representation, position, and dimensions.
 */

package com.GD.spaceshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Rock {
        Bitmap rock;
        Context context;
        int shx, shy;

        public Rock(Context context, int shx, int shy) {
            this.context = context;
            rock = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.rock);
            this.shx = shx;
            this.shy = shy;
        }
        public Bitmap getRock(){
            return rock;
        }
        public int getRockWidth() {
            return rock.getWidth();
        }
        public int getRockHeight() {
            return rock.getHeight();
        }
}

