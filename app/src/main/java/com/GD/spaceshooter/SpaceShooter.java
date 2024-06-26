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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import java.util.ArrayList;
import java.util.Random;

public class SpaceShooter extends View implements SensorEventListener{
    Context context;
    Bitmap background, lifeImage, pause_button;
    Handler handler;
    long UPDATE_MILLIS = 30;
    static int screenWidth, screenHeight;
    int points = 0;
    int life = 3;
    Paint scorePaint;
    int TEXT_SIZE = 80;
    boolean paused = false;
    OurSpaceship ourSpaceship;
    EnemySpaceship enemySpaceship;
    ArrayList<EnemySpaceship> enemySpaceships = new ArrayList<>();

    Random random;
    ArrayList<Shot> ourShots;
    ArrayList<Rock> enemyRocks;

    Explosion explosion;
    ArrayList<Explosion> explosions;
    boolean enemyShotAction = false;

    int lastDropScore = 0; // Track the score at the last drop to calculate the interval.

    //sensors for moving the ship based on tilting of the phone
    SensorManager sensorManager;
    Sensor accelerometer;


    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
           invalidate();
        }
    };


    public SpaceShooter(Context context) {
        super(context);
        this.context = context;
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        random = new Random();
        enemyRocks = new ArrayList<>();
        ourShots = new ArrayList<>();
        explosions = new ArrayList<>();
        ourSpaceship = new OurSpaceship(context);
        enemySpaceship = new EnemySpaceship(context);
        handler = new Handler();
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
        pause_button = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause_button);
        lifeImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        scorePaint = new Paint();
        scorePaint.setColor(Color.parseColor("#89CFF0"));
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        enemySpaceships.add(new EnemySpaceship(context)); // Adds the initial enemy spaceship

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw background, Points and life on Canvas
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(pause_button, screenWidth - pause_button.getWidth() - 20, 20, null);

        canvas.drawText("Pt: " + points, 0, TEXT_SIZE, scorePaint);
        for(int i=life; i>=1; i--){
            canvas.drawBitmap(lifeImage, screenWidth - lifeImage.getWidth() * i, 0, null);
        }
        // When life becomes 0, stop game and launch GameOver Activity with points
        if(life == 0){
//            paused = true;
            handler = null;
            Intent intent = new Intent(context, GameOver.class);
            intent.putExtra("points", points);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
        if (points % 5 == 0 && points != lastDropScore) {
            enemySpaceships.add(new EnemySpaceship(context));
            lastDropScore = points; // Update lastDropScore to prevent adding more than one spaceship per threshold
        }

        for (EnemySpaceship enemySpaceship : enemySpaceships) {
            // Update the position of the enemy spaceship
            enemySpaceship.ex += enemySpaceship.enemyVelocity;

            // Check for collision with the screen boundaries and reverse velocity if needed
            if (enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() >= screenWidth || enemySpaceship.ex <= 0) {
                enemySpaceship.enemyVelocity *= -1;
            }

            // Draw the enemy spaceship
            canvas.drawBitmap(enemySpaceship.getEnemySpaceship(), enemySpaceship.ex, enemySpaceship.ey, null);

            // Use the updated canDropRock() method to decide when to drop rocks
            if (enemySpaceship.canDropRock()) {
                Rock enemyRock = new Rock(context, enemySpaceship.ex + enemySpaceship.getEnemySpaceshipWidth() / 2, enemySpaceship.ey);
                enemyRocks.add(enemyRock);
                // No need to reset a global flag; the spaceship manages its own rock dropping cooldown
            }

        }


        // Draw our Spaceship
        canvas.drawBitmap(ourSpaceship.getOurSpaceship(), ourSpaceship.ox, ourSpaceship.oy, null);
        // Draw the enemy rock downwards our spaceship and if it's being hit, decrement life, remove
        // the rock object from enemyShots ArrayList and show an explosion.
        // Else if, it goes away through the bottom edge of the screen also remove
        // the shot object from enemyShots.
        // When there is no enemyShots no the screen, change enemyShotAction to false, so that enemy
        // can shot.
        for(int i=0; i < enemyRocks.size(); i++){
            enemyRocks.get(i).shy += 15;
            canvas.drawBitmap(enemyRocks.get(i).getRock(), enemyRocks.get(i).shx, enemyRocks.get(i).shy, null);
            if((enemyRocks.get(i).shx >= ourSpaceship.ox)
                && enemyRocks.get(i).shx <= ourSpaceship.ox + ourSpaceship.getOurSpaceshipWidth()
                && enemyRocks.get(i).shy >= ourSpaceship.oy
                && enemyRocks.get(i).shy <= screenHeight){
                life--;
                enemyRocks.remove(i);
                explosion = new Explosion(context, ourSpaceship.ox, ourSpaceship.oy);
                explosions.add(explosion);
            }else if(enemyRocks.get(i).shy >= screenHeight){
                points++;
                enemyRocks.remove(i);
            }
            if(enemyRocks.size() < 1){
                enemyShotAction = false;
            }
        }
        // Do the explosion
        for(int i=0; i < explosions.size(); i++){
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).eX, explosions.get(i).eY, null);
            explosions.get(i).explosionFrame++;
            if(explosions.get(i).explosionFrame > 8){
                explosions.remove(i);
            }
        }

        if(!paused)
            handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int)event.getX();

        // When event.getAction() is MotionEvent.ACTION_DOWN, control ourSpaceship
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            ourSpaceship.ox = touchX;
        }
        // When event.getAction() is MotionEvent.ACTION_MOVE, control ourSpaceship
        // along with the touch.
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            ourSpaceship.ox = touchX;
        }
        // Returning true in an onTouchEvent() tells Android system that you already handled
        // the touch event and no further handling is required.
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // In landscape mode, use event.values[1] to detect tilt left/right
            float tilt = event.values[1];  // Y-axis value, which corresponds to left/right tilt in landscape
            updateSpaceshipPosition(tilt);
        }
    }

    private void updateSpaceshipPosition(float tilt) {
        // Adjusting the spaceship's horizontal position based on the tilt
        // Multiply by a factor to control sensitivity and direction
        ourSpaceship.ox += tilt * 2; // Adjust the factor '2' to increase or decrease sensitivity

        // Make sure the spaceship stays within the screen bounds
        ourSpaceship.ox = Math.max(0, Math.min(ourSpaceship.ox, screenWidth - ourSpaceship.getOurSpaceshipWidth()));

        // Request to redraw the view
        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed but must be implemented
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
