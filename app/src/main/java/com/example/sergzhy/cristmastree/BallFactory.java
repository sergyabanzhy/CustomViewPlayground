package com.example.sergzhy.cristmastree;

import android.graphics.Paint;
import android.graphics.RadialGradient;


class BallFactory {
    static Ball create(float x, float y, int radius) {
        Paint paint = new Paint();
        RadialGradient gradient = new RadialGradient(x, y, radius, Utils.getColorRandomly(),
                Utils.getColorRandomly(), android.graphics.Shader.TileMode.CLAMP);
        paint.setDither(true);
        paint.setShader(gradient);
        return new Ball(x, y, radius, paint);
    }
}
