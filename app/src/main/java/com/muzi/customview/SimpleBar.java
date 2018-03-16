package com.muzi.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by muzi on 2018/3/15.
 * 727784430@qq.com
 */

public class SimpleBar extends View {

    private int firstColor;
    private int secondColor;
    private int circleWidth;
    private int circleSpeed;
    private Paint mPaint;
    private int mProgress;
    private boolean isNext;
    private boolean isStart = true;
    private RectF oval;
    private int center;
    private int radius;

    public SimpleBar(Context context) {
        this(context, null);
    }

    public SimpleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleBar);

        firstColor = typedArray.getColor(R.styleable.SimpleBar_firstColor, ContextCompat.getColor(context, R.color.colorAccent));
        secondColor = typedArray.getColor(R.styleable.SimpleBar_firstColor, ContextCompat.getColor(context, R.color.colorPrimary));
        circleSpeed = typedArray.getInt(R.styleable.SimpleBar_circleSpeed, 20);
        circleWidth = typedArray.getDimensionPixelSize(R.styleable.SimpleBar_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setStrokeWidth(circleWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心

        initRunnable();
    }

    private void initRunnable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    mProgress += 2;
                    if (mProgress >= 360) {
                        mProgress = 0;
                        isNext = !isNext;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(circleSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        center = getWidth() / 2;
        radius = center - circleWidth / 2;// 半径
        oval = new RectF(center - radius, center - radius, center + radius, center + radius); // 用于定义的圆弧的形状和大小的界限

        if (!isNext) {
            mPaint.setColor(firstColor); // 设置圆环的颜色
            canvas.drawCircle(center, center, radius, mPaint); // 画出圆环
            mPaint.setColor(secondColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        } else {
            mPaint.setColor(secondColor); // 设置圆环的颜色
            canvas.drawCircle(center, center, radius, mPaint); // 画出圆环
            mPaint.setColor(firstColor); // 设置圆环的颜色
            canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
        }
    }

    public void onDestroy() {
        isStart = false;
    }

}
