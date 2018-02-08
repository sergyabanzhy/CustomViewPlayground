package com.example.sergzhy.cristmastree;

import android.graphics.Canvas;
import android.graphics.Paint;

class Ball implements Item {
    private float mX;
    private float mY;
    private float mRadius;
    private Paint mColor;
    private float y;

    Ball(float x, float y, float radius, Paint color) {
        this.mX = x;
        this.mY = y;
        this.mRadius = radius;
        this.mColor = color;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(mX, mY, mRadius, mColor);
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public void setColor(Paint color) {
        this.mColor = color;
    }
}
