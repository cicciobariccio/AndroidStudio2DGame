package com.example.androidstudio2dgame.object;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.Context;

import com.example.androidstudio2dgame.GameLoop;

/**
 *  Circle is an abstract class which implements a draw method from GameObject for drawing the object as a Circle.
 */

public abstract class Circle extends GameObject {

    public static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.6;
    public static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    protected double radius;
    protected Paint paint;

    public Circle(Context context, int color, double positionX, double positionY, double radius) {
        super(positionX, positionY);
        this.radius = radius;

        // Set colors of circle
        paint = new Paint();
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }

}
