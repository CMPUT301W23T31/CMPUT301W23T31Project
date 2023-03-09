package com.example.cmput301w23t31project;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


// started with this code https://stackoverflow.com/questions/7344497/android-canvas-draw-rectangle
public class DrawRepresentation extends View {
    final String[] colors;
    final String overlay;
    final int height;

    Paint paint = new Paint();

    public DrawRepresentation(Context context, String hash, int boxHeight) {
        super(context);
        this.colors = new String[10];
        this.height = boxHeight;

        for (int i = 0; i < 10; i++) {
            this.colors[i] = "#" + hash.substring(6*i, 6*(i+1));
        }
        this.overlay = hash.substring(60, 64);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // drawing colored rectangles
        this.paint.setStrokeWidth(0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                this.paint.setColor(Color.parseColor(this.colors[j*5 + i]));
                canvas.drawRect(this.height*i, this.height*j, this.height*(i+1), this.height*(j+1), paint);
            }
        }

        // drawing white "frames"
        this.paint.setStrokeWidth(2);
        this.paint.setColor(0xffffff);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                canvas.drawRect(this.height*i, this.height*j, this.height*(i+1), this.height*(j+1), paint);
            }
        }

        // drawing text on top
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffffff);
        paint.setTextSize(this.height);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(overlay, this.height*2.5f, this.height, paint);

    }
}

