package com.example.androidstudio2dgame.gameobject;

import android.content.Context;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgame.GameDisplay;
import com.example.androidstudio2dgame.GameLoop;
import com.example.androidstudio2dgame.gamepanel.Joystick;
import com.example.androidstudio2dgame.R;
import com.example.androidstudio2dgame.Utils;
import com.example.androidstudio2dgame.gamepanel.HealthBar;
import com.example.androidstudio2dgame.graphics.Animator;
import com.example.androidstudio2dgame.graphics.Sprite;

/** Player is the main character of the game, which the user can control with a touch joystick.
 * The player class is an extension of a circle, which is an extension of a GameObject
 */

public class Player extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = 600.0;
    public static final int MAX_HEALTH_POINTS = 10;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;
    private HealthBar healthBar;
    private int healthPoints;
    private Animator animator;
    private PlayerState playerState;

    public Player(Context context, Joystick joystick, double positionY, double positionX, double radius, Animator animator) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        this.healthPoints = MAX_HEALTH_POINTS;
        this.animator = animator;
        this.playerState = new PlayerState(this);
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

        playerState.update();
    }

    @Override
    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        animator.draw(canvas, gameDisplay, this);



        healthBar.draw(canvas, gameDisplay);
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if (healthPoints >= 0){
            this.healthPoints = healthPoints;
        }

    }

    public PlayerState getPlayerState() {
        return playerState;
    }

}
