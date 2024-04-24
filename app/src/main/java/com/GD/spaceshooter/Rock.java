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

