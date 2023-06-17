package com.example.androidstudio2dgame;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 *  MainActivity is the entry point to our application
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set cintent view to game, so that objects in the game class can be rendered to the screen
        setContentView(new Game(this));
    }
}