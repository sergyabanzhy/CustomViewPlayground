package com.example.sergzhy.cristmastree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Random;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

class Utils {
    static int getColorRandomly() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT;
    }

    static int getPixelColor(Bitmap bitmap, MotionEvent event) {
        if (bitmap.getHeight() <= event.getY() || bitmap.getWidth() <= event.getX()) {
            return 0;
        }
        return bitmap.getPixel((int) event.getX(), (int) event.getY());
    }

    static boolean checkPixelColor(Bitmap bitmap, MotionEvent event) {
        if (event.getX() <= 0 || event.getY() <= 0) {
            return false;
        }
        int greenValue = Color.green(getPixelColor(bitmap, event));
        return greenValue > 100;
    }
}
