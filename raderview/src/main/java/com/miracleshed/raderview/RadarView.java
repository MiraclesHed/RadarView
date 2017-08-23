package com.miracleshed.raderview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 雷达图
 * 正六边形
 * Created by MiraclesHed on 2016/11/9.
 */

public class RadarView extends View {
    private final int DEFAULT_BRANCH_COLOR = Color.BLACK;
    private final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private final int DEFAULT_POINT_COLOR = Color.RED;
    private final int DEFAULT_CONTENT_COLOR = Color.RED;
    private final float DEFAULT_MAX_VALUE = 10;
    private final int DEFAULT_BORDER_COUNT = 5;
    private final int DEFAULT_SHAPE = 6;

    private int mBranchColor = DEFAULT_BRANCH_COLOR;//
    private int mBorderColor = DEFAULT_BORDER_COLOR;//
    private int mPointColor = DEFAULT_POINT_COLOR;//
    private int mContentColor = DEFAULT_CONTENT_COLOR;//

    private float mMaxValue = DEFAULT_MAX_VALUE; //最大值
    private int mBorderCount = DEFAULT_BORDER_COUNT;//多边形数量
    private int mShape = DEFAULT_SHAPE;//几边形

    private boolean mIsShowBranch = true;//是否显示以View的中心为起点绘制的线段
    private boolean mIsShowBorder = true;//是否显示多边形
    private boolean mIsShowPoint = true;//是否显示数据点

    private Paint mPaint;
    private int mCenterX;
    private int mCenterY;
    private int lineLenght;
    private int mRotateDegree = 60;

    private List<Point> pointList = new ArrayList<>();
    private float[] mValues;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView, defStyleAttr, defStyleRes);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.RadarView_branchColor) {
                mBranchColor = typedArray.getColor(index, DEFAULT_BRANCH_COLOR);

            } else if (index == R.styleable.RadarView_borderColor) {
                mBorderColor = typedArray.getColor(index, DEFAULT_BORDER_COLOR);

            } else if (index == R.styleable.RadarView_pointColor) {
                mPointColor = typedArray.getColor(index, DEFAULT_POINT_COLOR);

            } else if (index == R.styleable.RadarView_contentColor) {
                mContentColor = typedArray.getColor(index, DEFAULT_CONTENT_COLOR);

            } else if (index == R.styleable.RadarView_max) {
                mMaxValue = typedArray.getFloat(index, DEFAULT_MAX_VALUE);

            } else if (index == R.styleable.RadarView_borderCount) {
                mBorderCount = typedArray.getInt(index, DEFAULT_BORDER_COUNT);

            } else if (index == R.styleable.RadarView_shapeType) {
                mShape = typedArray.getInt(index, DEFAULT_SHAPE);

            } else if (index == R.styleable.RadarView_isShowBranch) {
                mIsShowBranch = typedArray.getBoolean(index, true);

            } else if (index == R.styleable.RadarView_isShowBorder) {
                mIsShowBorder = typedArray.getBoolean(index, true);

            } else if (index == R.styleable.RadarView_isShowPoint) {
                mIsShowPoint = typedArray.getBoolean(index, true);

            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mValues = new float[mShape];
        pointList = new ArrayList<>();
        mRotateDegree = 360 / mShape;
    }

    public void setValues(float[] values) {
        if (values == null || values.length == 0 || values.length > mShape) {
            return;
        }
        if (pointList == null) {
            pointList = new ArrayList<>();
        }
        mValues = values;
        pointList.clear();
        pointList.addAll(getPointList(mValues));
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
        lineLenght = Math.min(w, h) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        canvas.translate(mCenterX, mCenterY);

        //以View的中心为起点绘制六条线段
        if (mIsShowBranch) {
            drawBranch(canvas);
        }
        //由大到小绘制六个正六边形
        if (mIsShowBorder) {
            drawHexagon(canvas);
        }
        //在雷达图中绘制六个定位点
        if (mIsShowPoint) {
            drawPoint(canvas);
        }
        //把六个定位点用线连起来
        drawRegion(canvas);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void drawBranch(Canvas canvas) {
        mPaint.setColor(mBranchColor);
        for (int i = 1; i <= mShape; i++) {
            canvas.drawLine(0, 0, lineLenght, 0, mPaint);
            canvas.rotate(mRotateDegree);
        }
        canvas.rotate(180);
    }

    private void drawRegion(Canvas canvas) {
        mPaint.setColor(mContentColor);
        mPaint.setAlpha(130);
        Path path1;
        if (pointList != null && pointList.size() >= 3) {
            path1 = new Path();
            path1.moveTo(pointList.get(0).x, pointList.get(0).y);
            for (int i = 1; i < pointList.size(); i++) {
                path1.lineTo(pointList.get(i).x, pointList.get(i).y);
            }
            canvas.drawPath(path1, mPaint);
        }

    }

    private void drawPoint(Canvas canvas) {
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mPointColor);
        for (int i = 0; i < pointList.size(); i++) {
            canvas.drawCircle(pointList.get(i).x, pointList.get(i).y, 6, mPaint);
        }
    }

    private List<Point> getPointList(float[] values) {
        List<Point> list = new ArrayList<>();

        list.add(new Point((int) (lineLenght * values[0] / mMaxValue + 0.5f), 0));
        list.add(new Point((int) ((lineLenght * values[1] / mMaxValue + 0.5f) * 0.5), -(int) ((lineLenght * values[1] / mMaxValue + 0.5f) * Math.sin(Math.PI / 3))));
        list.add(new Point(-(int) ((lineLenght * values[2] / mMaxValue + 0.5f) * 0.5), -(int) ((lineLenght * values[2] / mMaxValue + 0.5f) * Math.sin(Math.PI / 3))));
        list.add(new Point(-(int) (lineLenght * values[3] / mMaxValue + 0.5f), 0));
        list.add(new Point(-(int) ((lineLenght * values[4] / mMaxValue + 0.5f) * 0.5), (int) ((lineLenght * values[4] / mMaxValue + 0.5f) * Math.sin(Math.PI / 3))));
        list.add(new Point((int) ((lineLenght * values[5] / mMaxValue + 0.5f) * 0.5), (int) ((lineLenght * values[5] / mMaxValue + 0.5f) * Math.sin(Math.PI / 3))));

        return list;
    }

    private void drawHexagon(Canvas canvas) {
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = mBorderCount; i >= 1; i--) {
            for (int j = 1; j <= mShape; j++) {
                canvas.drawLine(
                        lineLenght * i / mBorderCount,
                        0,
                        lineLenght * i / mBorderCount / 2,
//                        (float) (lineLenght * i / mBorderCount * Math.sqrt(3) * 0.5f),
                        (float) (lineLenght * i / mBorderCount * Math.sqrt(3) / 2),
                        mPaint);
                canvas.rotate(60);
            }
        }
    }

}
