package com.example.sergzhy.cristmastree;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class TreeView extends View {
    public static final String TAG = TreeView.class.getSimpleName();

    private Bitmap mSnowflakeMarker;
    private Bitmap mTreeBitmap;
    private RectF mRectF;
    private ArrayList<Ball> mBalls;
    private float mSnowflakeMotionCoordinateY = 0f;
    private final int LINE_WIDTH = 30;
    private Paint mPaintLine;
    private boolean mIsSnowflakeHold = false;
    private Timer mTimer;
    private boolean mIsTimerStarted;
    private float mRadius = 20;

    public TreeView(Context context) {
        super(context);
        init();
    }

    public TreeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLighting();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure, width = " + View.MeasureSpec.getSize(widthMeasureSpec) + " , height " + View.MeasureSpec.getSize(heightMeasureSpec) + "");

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged, w, " + w +", h " + h +"");
        super.onSizeChanged(w, h, oldw, oldh);
        resizeTreeBitmap(w, h);
        calculateSnowflakePosition();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);

        canvas.drawRect(mRectF, mPaintLine);
        canvas.drawBitmap(mTreeBitmap, 0, 0, null);

        for (Ball ball : mBalls) {
            ball.draw(canvas);
        }

        drawSnowflakeMarker(canvas);
    }

    private void init(){
        Log.d(TAG, "init()");

        mBalls = new ArrayList<>();

        mTimer = new Timer();

        mSnowflakeMarker = BitmapFactory.decodeResource(getResources(), R.drawable.snowflake);

        mTreeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tree);

        initSnowflakeTrajectory();
    }

    private void drawSnowflakeMarker(Canvas canvas) {
        canvas.drawBitmap(mSnowflakeMarker, mRectF.centerX() - mSnowflakeMarker.getHeight()/2, mSnowflakeMotionCoordinateY, mPaintLine);
    }

    private void initSnowflakeTrajectory() {
        mPaintLine = new Paint();
        mPaintLine.setStrokeWidth(LINE_WIDTH);
        mPaintLine.setColor(Color.CYAN);
        mPaintLine.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void createBall(float x, float y) {
        Log.d(TAG, "createBall");
        mBalls.add(BallFactory.create(x, y, (int) mRadius));
    }

    private void resizeTreeBitmap(int width, int height) {
        Log.d(TAG, "resizeTreeBitmap, width = " + width +", height = " + height +"");

        mTreeBitmap = Bitmap.createScaledBitmap(mTreeBitmap, width, height, false);
    }

    private void calculateSnowflakePosition() {
        mRectF = new RectF(getWidth() - mSnowflakeMarker.getHeight()/2, 0,
                getWidth() - mSnowflakeMarker.getHeight()/2 + LINE_WIDTH, getHeight());
    }

    private int calculateSnowflakePathPercentage() {
        int maximum = getMeasuredHeight() - mSnowflakeMarker.getHeight();
        Log.d(TAG, "coveredPathPercentage, " + mSnowflakeMotionCoordinateY * 100 / maximum +"");

        return (int) (mSnowflakeMotionCoordinateY * 100 / maximum);
    }

    private void resizeBalls() {
        for (Ball b : mBalls) {
            b.setRadius((int) mRadius);
        }
    }

    private void configureBall() {
        Log.d(TAG, "configureBall");

        for (Ball ball : mBalls) {
            Paint paint = new Paint();
            RadialGradient gradient = new RadialGradient(ball.getX(), ball.getY(), mRadius,
                    Utils.getColorRandomly(), Utils.getColorRandomly(), android.graphics.Shader.TileMode.CLAMP);

            paint.setDither(true);
            paint.setShader(gradient);
            ball.setColor(paint);
        }
    }
    public void clearCanvas() {
        Log.d(TAG, "clearCanvas");
        stopLighting();
        mBalls.clear();
        invalidate();
    }

    private void stopLighting() {
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
        mTimer = new Timer();
        mIsTimerStarted = false;
    }

    private void startLighting() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getHandler().post(new Runnable() {
                    @Override
                    public void run() {

                        configureBall();
                        invalidate();
                    }
                });
            }
        }, 1, 200);

        mIsTimerStarted = true;
    }

    public void lightingControl() {

        if (!mIsTimerStarted && !mBalls.isEmpty()) {
            startLighting();
        } else {
            stopLighting();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent, event "+ event.toString() +"");
        int eventAction = event.getAction();

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:

                if (mIsSnowflakeHold) {
                    mIsSnowflakeHold = false;
                    break;
                }

                if (Utils.checkPixelColor(mTreeBitmap, event)) {
                    createBall(x, y);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:

                //bottom/top snowflake border
                if ((getMeasuredHeight() - mSnowflakeMarker.getHeight() <= y) || (0 > y)) {
                    break;
                }

                if (mIsSnowflakeHold) {

                    mSnowflakeMotionCoordinateY = y;
                    mRadius = calculateSnowflakePathPercentage();
                    resizeBalls();

                    invalidate();
                } else {

                    if (mRectF.contains(event.getX(), event.getY())) {
                        mIsSnowflakeHold = true;
                    }
                }

                break;
        }

        return true;
    }
}
