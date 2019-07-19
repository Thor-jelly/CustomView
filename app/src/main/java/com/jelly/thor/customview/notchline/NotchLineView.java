package com.jelly.thor.customview.notchline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.jelly.thor.customview.R;

/**
 * 类描述：缺口线<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/7/12 10:20 <br/>
 */
public class NotchLineView extends View {
    private int defaultWidthSize = dp2px(100);
    private int defaultHeightSize = dp2px(20);

    /**
     * 边框画笔
     */
    private Paint mPaint;
    /**
     * 路径
     */
    private Path mPath;

    /**
     * 虚线画笔
     */
    private Paint mStockPaint;
    /**
     * 背景色
     */
    private int mBgColor;
    /**
     * 虚线颜色
     */
    private int mDashColor;
    /**
     * 虚线画笔宽度
     */
    private float mDashPaintWidth;
    /**
     * 虚线宽度
     */
    private float mDashWidth;

    public NotchLineView(Context context) {
        this(context, null);
    }

    public NotchLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotchLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NotchLineView);
        mBgColor = typedArray.getColor(R.styleable.NotchLineView_bg_color, Color.YELLOW);
        mDashColor = typedArray.getColor(R.styleable.NotchLineView_dash_color, Color.GRAY);
        mDashPaintWidth = typedArray.getDimension(R.styleable.NotchLineView_dash_height, dp2px(1));
        mDashWidth = typedArray.getDimension(R.styleable.NotchLineView_dash_width, dp2px(2));
        typedArray.recycle();

        initPaint();
        initPath();
    }

    private void initPath() {
        mPath = new Path();
        mPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);
        mPaint.setStrokeWidth(dp2px(1));

        mStockPaint = new Paint();
        mStockPaint.setStyle(Paint.Style.STROKE);
        mStockPaint.setColor(mDashColor);
        mStockPaint.setStrokeWidth(mDashPaintWidth);
        mStockPaint.setPathEffect(new DashPathEffect(new float[]{mDashWidth, mDashWidth}, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        int width = getMeasuredWidth();
        //直径
        int height = getMeasuredHeight();
        //半径
        int radius = height / 2;
        mPath.addArc(new RectF(width - radius, 0, width + radius, height), 90, 180);
        mPath.addArc(new RectF(0 - radius, 0, 0 + radius, height), -90, 180);
        canvas.drawPath(mPath, mPaint);
        canvas.drawLine(radius + dp2px(4), radius, width - radius - dp2px(4), radius, mStockPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getNewWidth(widthMeasureSpec), getNewHeight(heightMeasureSpec));
    }

    private int getNewHeight(int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                //具体值
                return MeasureSpec.getSize(heightMeasureSpec);
            case MeasureSpec.AT_MOST:
                //默认值
                return defaultHeightSize;
            case MeasureSpec.UNSPECIFIED:
                return Math.max(defaultHeightSize, MeasureSpec.getSize(heightMeasureSpec));
            default:
                return defaultHeightSize;
        }
    }

    private int getNewWidth(int widthMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                //具体值
                return MeasureSpec.getSize(widthMeasureSpec);
            case MeasureSpec.AT_MOST:
                //默认值
                return defaultWidthSize;
            case MeasureSpec.UNSPECIFIED:
                //默认值
                return Math.max(defaultWidthSize, MeasureSpec.getSize(widthMode));
            default:
                return defaultWidthSize;
        }
    }

    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getContext().getResources().getDisplayMetrics());
    }
}
