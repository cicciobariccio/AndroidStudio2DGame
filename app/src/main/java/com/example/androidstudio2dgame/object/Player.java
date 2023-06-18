package com.example.androidstudio2dgame.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgame.GameLoop;
import com.example.androidstudio2dgame.Joystick;
import com.example.androidstudio2dgame.R;
import com.example.androidstudio2dgame.Utils;

/** Player is the main character of the game, which the user can control with a touch joystick.
 * The player class is an extension of a circle, which is an extension of a GameObject
 */

public class Player extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;

    public Player(Context context, Joystick joystick, double positionY, double positionX, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);

        this.joystick = joystick;
    }

    public void update() {
        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;

        // Update position
        positionX += velocityX;
        positionY += velocityY;

        // Update direction
        if (velocityX != 0 || velocityY != 0){
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }
    }
}
