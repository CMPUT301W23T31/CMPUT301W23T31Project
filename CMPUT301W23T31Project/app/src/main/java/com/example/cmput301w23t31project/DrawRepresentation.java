package com.example.cmput301w23t31project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Represents and creates unique visualization for QR code (based off of QR code hash)
 * Stores info about how/what to draw, and draws in real-time when needed
 * started with this code https://stackoverflow.com/questions/7344497/android-canvas-draw-rectangle
 */
public class DrawRepresentation extends Drawable {
    final String[] colors;
    final String overlay;
    final int height;

    Paint paint = new Paint();

    /**
     * Creates and stores relevant info needed for drawing
     * @param hash hash of QR code to create visualization for
     * @param boxHeight height to draw boxes on screen
     */
    public DrawRepresentation(String hash, int boxHeight) {
        this.colors = new String[10];
        this.height = boxHeight;

        // creating 10 RGB codes and 1 4-hex_character string
        for (int i = 0; i < 10; i++) {
            this.colors[i] = "#" + hash.substring(6*i, 6*(i+1));
        }
        this.overlay = hash.substring(60, 64);
    }

    /**
     * Draws the visual representation in real time using pre-stored info
     * @param canvas canvas to draw visual representation on
     */
    @Override
    public void draw(@NonNull Canvas canvas) {

        /* General Schema/Pattern
         * -----------------------------------------
         * |       |       |       |       |       |
         * |  RGB  |  RGB  |  RGB  |  RGB  |  RGB  |
         * |       |       |       |       |       |
         * ------- A ----- B ----- C ----- D -------
         * |       |       |       |       |       |
         * |  RGB  |  RGB  |  RGB  |  RGB  |  RGB  |
         * |       |       |       |       |       |
         * -----------------------------------------
         */

        // drawing colored rectangles
        this.paint.setStrokeWidth(0);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                this.paint.setColor(Color.parseColor(this.colors[j*5 + i]));
                canvas.drawRect(this.height*i, this.height*j, this.height*(i+1), this.height*(j+1), paint);
            }
        }

        // drawing white "frames" on top of rectangles
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(2);
        this.paint.setColor(Color.parseColor("#ffffff"));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                canvas.drawRect(this.height*i, this.height*j, this.height*(i+1), this.height*(j+1), paint);
            }
        }

        // drawing text on top of grid (with ~40% transparency)
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setColor(Color.parseColor("#99000000"));
        this.paint.setTextSize((int)(height*1.8f));
        this.paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(overlay.toUpperCase(), this.height*2.5f, this.height*1.7f, paint);

    }

    // Really not needed
    @Override
    public void setAlpha(int i) {}
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {}
    @Override
    public int getOpacity() {return PixelFormat.OPAQUE;}
}

