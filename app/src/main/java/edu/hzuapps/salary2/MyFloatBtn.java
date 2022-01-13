package edu.hzuapps.salary2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFloatBtn extends FloatingActionButton {
    private static final String TAG = "MyFloatBtn";
    private int mLastX, mLastY;//按下时的X，Y坐标
    private int mDownX, mDownY;//按下时的X，Y坐标 用来计算移动的距离
    private int mScreenWidth, mScreenHeight;//ViewTree的宽和高
    // 重写了所有的构造函数，因为不知道会用哪种
    // 每个构造函数都进行了数据的初始化，即获取屏幕的高度和宽度
    public MyFloatBtn(Context context) {
        super(context);
        initData(context);
    }

    public MyFloatBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public MyFloatBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }
    // 初始化数据：高度和宽度。看上去没啥问题，但是运行的时候浮动按钮的一部分会跑到虚拟导航栏的下面，也就是说获取的高度超过了显示的部分，但是宽度是正常的。
    private void initData(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mScreenHeight = metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;
    }

    // 重写onTouchEvent方法，主要在这个方法里面来处理点击和拖动事件
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        int actionId = ev.getAction();
        // Log.i(TAG, "onTouchEvent: " + actionId);
        switch (actionId) {
            // 无论是拖动还是点击 都需要先按下去，所以在这个case里记录按钮按下时的坐标
            case MotionEvent.ACTION_DOWN:
                // Log.i(TAG, "onTouchEvent: " + "DOWN");
                mLastX = (int) ev.getRawX();// getRawX: 获取的距离屏幕边缘的距离，而不是组件的
                mLastY = (int) ev.getRawY();
                mDownX = mLastX;// 判断是点击事件还是拖动事件时需要用到
                mDownY = mLastY;
                break;
            // 抬起事件：无论是拖动还是点击，完成之后都需要抬起按钮，在这个case里判断是拖动按钮还是点击按钮，主要是通过移动的距离来判断的，如果移动的距离超过5则认为是拖动，否则就是点击事件。如果是拖动事件则需要在此消费掉此事件，不会再执行onClick的方法。
            case MotionEvent.ACTION_UP:
                // Log.i(TAG, "onTouchEvent: " + "UP");
                if (calculateDistance(mDownX, mDownY, (int) ev.getRawX(), (int) ev.getRawY()) <= 5) {
                    Log.i(TAG, "onTouchEvent: 点击事件");
                } else {
                    Log.i(TAG, "onTouchEvent: 拖动事件");
                    // 消费掉此事件
                    return true;
                }
                break;
            // 移动事件：根据移动之后的位置进行重绘
            case MotionEvent.ACTION_MOVE:
                int dx = (int) ev.getRawX() - mLastX;//x方向的偏移量
                int dy = (int) ev.getRawY() - mLastY;//y方向的偏移量
                // getLeft(): Left position of this view relative to its parent.
                // 计算组件此时的位置，距离父容器上下左右的距离=偏移量 + 原来的距离
                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                /*if (dy < 0) {
                    Log.i(TAG, "onTouchEvent: 向上拖动");
                } else {
                    Log.i(TAG, "onTouchEvent: 向下拖动");
                }
                if (dx < 0) {
                    Log.i(TAG, "onTouchEvent: 向左拖动");
                } else {
                    Log.i(TAG, "onTouchEvent: 向右拖动");
                }*/
                mLastX = (int) ev.getRawX();
                mLastY = (int) ev.getRawY();
                if (top < 0) {
                    // 移出了上边界
                    top = 0;
                    bottom = getHeight();
                }
                if (left < 0) {
                    // 移出了左边界
                    left = 0;
                    right = getWidth();
                }
                if (bottom > mScreenHeight) {
                    // 移出了下边界
                    bottom = mScreenHeight;
                    top = bottom - getHeight();
                }
                if (right > mScreenWidth) {
                    // 移出了右边界
                    right = mScreenWidth;
                    left = right - getWidth();
                }
                layout(left, top, right, bottom);
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    // get the distance between (downX, downY) and (lastX, lastY)
    private int calculateDistance(int downX, int downY, int lastX, int lastY) {
        return (int) Math.sqrt(Math.pow(1.0f * (lastX - downX), 2.0) + Math.pow((lastY - downY) * 1.0f, 2.0));
    }
}

