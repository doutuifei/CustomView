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

public class MutipleBar extends View {

    private Context context;
    private int circleWidth;
    private int circleSpeed;
    private Paint mPaint;
    private int mProgress;
    private int firstColor = 0;
    private int secondColor = 1;
    private boolean isStart;
    private int[] colors;
    private RectF oval;
    private int center;
    private int radius;
    private Thread thread;

    public MutipleBar(Context context) {
        this(context, null);
    }

    public MutipleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MutipleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleBar);

        circleSpeed = typedArray.getInt(R.styleable.SimpleBar_circleSpeed, 20);
        circleWidth = typedArray.getDimensionPixelSize(R.styleable.SimpleBar_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setStrokeWidth(circleWidth); // 设置圆环的宽度
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 设置空心
    }

    private void initRunnable() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStart) {
                    mProgress += 2;
                    if (mProgress >= 360) {
                        mProgress = 0;
                        firstColor++;
                        secondColor++;
                        if (firstColor == colors.length) {
                            firstColor = 0;
                        }
                        if (secondColor == colors.length) {
                            secondColor = 0;
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(circleSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colors == null || colors.length < 2) {
            return;
        }
        center = getWidth() / 2;
        radius = center - circleWidth / 2;// 半径
        oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        mPaint.setColor(ContextCompat.getColor(context, colors[firstColor])); // 设置圆环的颜色
        canvas.drawCircle(center, center, radius, mPaint); // 画出圆环
        mPaint.setColor(ContextCompat.getColor(context, colors[secondColor])); // 设置圆环的颜色
        canvas.drawArc(oval, -90, mProgress, false, mPaint); // 根据进度画圆弧
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void start() {
        if (thread == null) {
            isStart = true;
            initRunnable();
            thread.start();
        }
    }

    public void onDestroy() {
        isStart = false;
    }

}
