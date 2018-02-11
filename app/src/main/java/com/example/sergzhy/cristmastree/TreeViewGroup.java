package com.example.sergzhy.cristmastree;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TreeViewGroup extends ViewGroup {
    private static final String TAG = TreeViewGroup.class.getSimpleName();
    private int mRequestedChildSpace;
    private boolean mIsHalfRowFree = false;

    public TreeViewGroup(Context context) {
        super(context);
    }

    public TreeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TreeViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure, width = " + View.MeasureSpec.getSize(widthMeasureSpec) +" , height" + View.MeasureSpec.getSize(heightMeasureSpec) +"");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        View view = null;
        int requestedHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {

            if (getChildAt(i) instanceof TreeView) {
                view = getChildAt(i);
            } else  {
                if (requestedHeight > getChildAt(i).getLayoutParams().height) {
                    return;
                }
                requestedHeight = getChildAt(i).getLayoutParams().height;
            }
        }

        if(!Utils.isPortrait(getContext()) && view != null) {
            view.measure(widthMeasureSpec/2, View.MeasureSpec.getSize(heightMeasureSpec));
        } else if (view != null){
            view.measure(widthMeasureSpec, heightMeasureSpec - requestedHeight);
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG,"onLayout, left: " + left +", top: " + top +", right: " + right +", bottom: " + bottom +"");
        mRequestedChildSpace = bottom;

        View view;
        for (int i = 0; i < getChildCount(); i++) {
            if ((getChildAt(i) instanceof Button)) {

                view = getChildAt(i);

                if (!Utils.isPortrait(getContext())) {

                    //if it landscape children should be drawn in a right part of screen
                    layoutChildMatchingWidthView(view, right / 2, right);

                } else {

                    layoutChildInPortrait(view, left, right);
                }
            }

            if (getChildAt(i) instanceof TreeView) {
                getChildAt(i).layout(0, 0, getChildAt(i).getMeasuredWidth(), getChildAt(i).getMeasuredHeight());
            }
        }
    }

    private boolean shouldViewBeAsParent(View view, int right) {
        Log.d(TAG,"shouldViewBeAsParent = " + (view.getLayoutParams().width > right/2) + "");
        //check view is more than a half of the parent
        return view.getLayoutParams().width > right/2;
    }

    private void layoutChildMatchingWidthView(View view, int left, int right) {
        view.layout(left + view.getPaddingLeft(), mRequestedChildSpace - view.getLayoutParams().height,
                right - view.getPaddingRight(), mRequestedChildSpace);

        mRequestedChildSpace -= view.getLayoutParams().height;
    }

    private void layoutMatchHalfWidthView(View view, int left ,int right) {
        view.layout(left + view.getPaddingLeft(), mRequestedChildSpace - view.getLayoutParams().height,
                right/ 2, mRequestedChildSpace);

        mIsHalfRowFree = true;
    }

    private void layoutSecondHalfWidthView(View view, int right) {
        view.layout(right/ 2, mRequestedChildSpace - view.getLayoutParams().height,
                right - view.getPaddingRight(), mRequestedChildSpace);

        mRequestedChildSpace -= view.getLayoutParams().height;

        mIsHalfRowFree = false;
    }

    private void layoutChildInPortrait (View view, int left, int right) {

        if (mIsHalfRowFree) {

            layoutSecondHalfWidthView(view,right);

        } else {

            if (shouldViewBeAsParent(view, right)) {
                layoutChildMatchingWidthView(view, left,right);
            } else {
                layoutMatchHalfWidthView(view, left, right);
            }
        }
    }
}
