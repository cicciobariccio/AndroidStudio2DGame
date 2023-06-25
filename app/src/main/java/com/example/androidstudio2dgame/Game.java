package com.example.androidstudio2dgame;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgame.gamepanel.Joystick;
import com.example.androidstudio2dgame.gameobject.Circle;
import com.example.androidstudio2dgame.gameobject.Enemy;
import com.example.androidstudio2dgame.gamepanel.GameOver;
import com.example.androidstudio2dgame.gameobject.Player;
import com.example.androidstudio2dgame.gameobject.Spell;
import com.example.androidstudio2dgame.gamepanel.Performance;
import com.example.androidstudio2dgame.graphics.Animator;
import com.example.androidstudio2dgame.graphics.SpriteSheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  Game manages all objects in the game and is responsible for updateing all states and render all objects to screen
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final Joystick joystick;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Spell> spellList = new ArrayList<Spell>();
    private int joystickPointerId = 0;
    private int numberOfSpellToCast = 0;
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;

    public Game(Context context) {
        super(context);

        // get surface holder and add callback
        SurfaceHolder surfaceholder = getHolder();
        surfaceholder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceholder);

        // Initialize game panels
        gameOver = new GameOver(context);
        joystick = new Joystick(275,700,70,40);
        performance = new Performance(gameLoop, context);

        // Initialize Game objects
        SpriteSheet spriteSheet = new SpriteSheet(context);
        Animator animator = new Animator(spriteSheet.getPlayerSpriteArray());

        player = new Player(context, joystick, 500, 500, 30, animator);

        // Initialize game display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // handle touch event actions
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed()) {
                    // joystick was pressed before this event -> cast spell
                    numberOfSpellToCast++;
                } else if(joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    // joystick is pressed in this event -> setIsPressed(true) and store ID
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    // joystick was not previously, and is not pressed in this event -> case spell
                    spellList.add(new Spell(getContext(), player));
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystickPointerId == event.getPointerId(event.getActionIndex())) {
                    // joystick was let go of -> setIsPressed(false) and resetActuator
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {

            gameLoop = new GameLoop(this, holder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        performance.drawUPS(canvas);
        performance.drawFPS(canvas);

        // draw game panels 
        joystick.draw(canvas);
        performance.draw(canvas);
        
        // draw game object
        player.draw(canvas, gameDisplay);

        // Draw each enemy
        for (Enemy enemy : enemyList) {
            enemy.draw(canvas, gameDisplay);
        }
        // Draw each spell
        for (Spell spell : spellList) {
            spell.draw(canvas, gameDisplay);
        }

        // Draw GameOver
        if (player.getHealthPoints() <= 0){
            gameOver.Draw(canvas);
        }
        
    }

    public void update() {
        // Update game state

        if (player.getHealthPoints() <= 0){
            return;
        }

        joystick.update();

        player.update();

        // Spawn enemy if is time to spawn new enemies
        if (Enemy.readyToSpawn()) {
            enemyList.add(new Enemy(getContext(), player));
        }

        while (numberOfSpellToCast > 0){
            spellList.add(new Spell(getContext(), player));
            numberOfSpellToCast --;
        }

        // Update state of each enemy
        for (Enemy enemy : enemyList) {
            enemy.update();
        }

        // Update state of each spell
        for (Spell spell :spellList) {
            spell.update();
        }

        // Iterate through enemyList and check for collision between each enemy and player and all spells
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            Circle enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)) {
                iteratorEnemy.remove();
                player.setHealthPoints(player.getHealthPoints() - 1);
                continue;
            }

            Iterator<Spell> iteratorSpell = spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Circle spell = iteratorSpell.next();
                if (Circle.isColliding(spell, enemy)) {
                    iteratorEnemy.remove();
                    iteratorSpell.remove();
                    break;
                }
            }

        }

        gameDisplay.update();

    }

    public void pause() {
        gameLoop.stopLoop();
    }
}
