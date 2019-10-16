package com.jelly.thor.customview.loadinganimation00

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.min
import kotlin.math.sqrt

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/10/15 20:01 <br/>
 */
class ShapeView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    /**
     * 形状枚举
     */
    enum class ShapeEnum {
        /**
         * 圆
         */
        CIRCLE,
        /**
         * 方
         */
        SQUARE,
        /**
         * 三角
         */
        TRIANGLE
    }

    /**
     * 当前形状，默认圆形
     */
    var mShapeEnum = ShapeEnum.CIRCLE
        private set

    /**
     * 当前的画笔
     */
    private val mPaint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint
    }

    /**
     * 封闭等边三角形
     */
    private val mTrianglePath by lazy {
        val path = Path()
        path.moveTo((width / 2).toFloat(), 0F)
        //tan60 * (width / 2)
        val triangleHeight = width / 2F * sqrt(3F)
        path.lineTo(0F, triangleHeight)
        path.lineTo(width.toFloat(), triangleHeight)
        path.close()
        path
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //设置大小
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //设置成为正方形
        val minSize = min(widthSize, heightSize)
        setMeasuredDimension(minSize, minSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制三种形状
        when (mShapeEnum) {
            ShapeEnum.CIRCLE -> {
                //画圆
                mPaint.color = Color.RED
                val center = width / 2
                canvas?.drawCircle(center.toFloat(), center.toFloat(), center.toFloat(), mPaint)
            }
            ShapeEnum.SQUARE -> {
                //画方
                mPaint.color = Color.GREEN
                canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mPaint)
            }
            ShapeEnum.TRIANGLE -> {
                //画三角
                mPaint.color = Color.BLUE
                canvas?.drawPath(mTrianglePath, mPaint)
            }
        }
    }

    /**
     * 执行改变
     */
    fun exchange() {
        mShapeEnum = when (mShapeEnum) {
            ShapeEnum.CIRCLE -> {
                ShapeEnum.SQUARE
            }
            ShapeEnum.SQUARE -> {
                ShapeEnum.TRIANGLE
            }
            ShapeEnum.TRIANGLE -> {
                ShapeEnum.CIRCLE
            }
        }
        invalidate()
    }
}

