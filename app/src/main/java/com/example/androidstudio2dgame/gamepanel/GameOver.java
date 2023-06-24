package com.example.androidstudio2dgame.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.androidstudio2dgame.R;

/**
 * GameOver us a panel whic draws the text Game Over to the screen
 */

public class GameOver {

    private Context context;

    public GameOver(Context context) {
        this.context = context;
    }

    public void Draw(Canvas canvas) {
        String text = "GameOver";

        float x = 800;
        float y = 200;

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.GameOver);
        paint.setColor(color);
        float textSize = 150;
        paint.setTextSize(textSize);
        canvas.drawText(text, x, y, paint);
    }
}
