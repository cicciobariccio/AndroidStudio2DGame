package com.example.androidstudio2dgame;

import android.graphics.Canvas;

/**
 * GameObject is an abstract class which is the foundation of all world objects in the game.
 */

public abstract class GameObject {

    protected double positionY;
    protected double positionX;
    protected double velocityX;
    protected double velocityY;

    public GameObject(double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();
}
