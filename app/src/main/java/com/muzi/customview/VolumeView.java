package com.muzi.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by muzi on 2018/3/15.
 * 727784430@qq.com
 */

public class VolumeView extends View {

    private int selectColor;
    private int unSelectColor;

    private int mCount;
    private int mCurrentCount;

    private float solidAngle;
    private float spaceAngle;
    private float itemAngle;

    private Rect mRect;
    private RectF oval;
    private Paint mPaint;
    private int xDown, xUp;
    private int circleWidth;
    private Bitmap centerBitmap;

    public VolumeView(Context context) {
        this(context, null);
    }

    public VolumeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VolumeView);
        unSelectColor = typedArray.getColor(R.styleable.VolumeView_unSelectColor, Color.BLACK);
        selectColor = typedArray.getColor(R.styleable.VolumeView_selectColor, Color.WHITE);
        circleWidth = typedArray.getDimensionPixelSize(R.styleable.SimpleBar_circleWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 15, getResources().getDisplayMetrics()));
        mCount = typedArray.getInt(R.styleable.VolumeView_allCount, 13);
        mCurrentCount = typedArray.getInt(R.styleable.VolumeView_curreCount, 3);
        solidAngle = typedArray.getFloat(R.styleable.VolumeView_solidAngle, 7f);
        centerBitmap = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(R.styleable.VolumeView_centerIcon, 0));
        typedArray.recycle();

        mPaint = new Paint();
        mRect = new Rect();

        if (listener != null) {
            listener.onLinstener(mCount, mCurrentCount);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(circleWidth); // 设置圆环的宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 定义线段断电形状为圆头
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE);//空心

        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = centre - circleWidth / 2;// 半径

        drawOval(canvas, centre, radius);

        int ovalRadius = radius - circleWidth / 2;// 内圆半径
        // 内切正方形的距离顶部 = mCircleWidth + relRadius - √2 / 2
        mRect.left = (int) (ovalRadius - Math.sqrt(2) * 1.0f / 2 * ovalRadius) + circleWidth;
        /**
         * 内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
         */
        mRect.top = (int) (ovalRadius - Math.sqrt(2) * 1.0f / 2 * ovalRadius) + circleWidth;
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * ovalRadius);
        mRect.right = (int) (mRect.left + Math.sqrt(2) * ovalRadius);

        /**
         * 如果图片比较小，那么根据图片的尺寸放置到正中心
         */
        if (centerBitmap.getWidth() < Math.sqrt(2) * ovalRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * ovalRadius * 1.0f / 2 - centerBitmap.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * ovalRadius * 1.0f / 2 - centerBitmap.getHeight() * 1.0f / 2);
            mRect.right = mRect.left + centerBitmap.getWidth();
            mRect.bottom = mRect.top + centerBitmap.getHeight();
        }
        // 绘图
        canvas.drawBitmap(centerBitmap, null, mRect, mPaint);
    }

    /**
     * 根据参数画出每个小块
     *
     * @param canvas
     * @param center
     * @param radius
     */
    private void drawOval(Canvas canvas, int center, int radius) {
        spaceAngle = (240f - mCount * solidAngle) / (mCount - 1);
        itemAngle = solidAngle + spaceAngle;

        oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        mPaint.setColor(unSelectColor);

        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(oval, i * itemAngle + 150, solidAngle, false, mPaint);
        }

        mPaint.setColor(selectColor);
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, i * itemAngle + 150, solidAngle, false, mPaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                xUp = (int) event.getY();
                if (xUp > xDown) {
                    if (xUp - xDown > itemAngle) {
                        down();
                    }
                } else {
                    if (xDown - xUp > itemAngle) {
                        up();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 当前数量+1
     */
    public void up() {
        if (mCurrentCount < mCount) {
            mCurrentCount++;
            invalidate();
            if (listener != null) {
                listener.onLinstener(mCount, mCurrentCount);
            }
        }
    }

    /**
     * 当前数量-1
     */
    public void down() {
        if (mCurrentCount > 0) {
            mCurrentCount--;
            invalidate();
            if (listener != null) {
                listener.onLinstener(mCount, mCurrentCount);
            }
        }
    }

    private VolumeListener listener;

    public interface VolumeListener {
        void onLinstener(int count, int curreCount);
    }

    public void setListener(VolumeListener listener) {
        this.listener = listener;
    }
}