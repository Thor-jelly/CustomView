package com.jelly.thor.customview.ninelockpalaces

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 类描述：九宫格view <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/7/16 14:26 <br/>
 */
class NineLockPalacesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    //    自定义属性
    /**
     * 外圆大小
     */
    private var mOutRadio = 0

    /**
     * 是否是第一次初始化，确保初始化只执行一次
     */
    private var mIsInit = false

    private val mPoints = Array(3) { Array<Point?>(3) { null } }

    //1.设置颜色
    private val mOuterPressedColor = 0xff8cbad8.toInt()
    private val mInnerPressedColor = 0xff0596f6.toInt()
    private val mOuterNormalColor = 0xffd9d9d9.toInt()
    private val mInnerNormalColor = 0xff929292.toInt()
    private val mOuterErrorColor = 0xff901032.toInt()
    private val mInnerErrorColor = 0xffea0945.toInt()
    //2.初始化画笔
    //2.1 线画笔
    private val mLinePaint by lazy {
        val paint = Paint()
        paint.color = mInnerPressedColor
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeWidth = (mOutRadio / 9).toFloat()
        paint
    }
    //2.2 按下的画笔
    private val mPressedPaint by lazy {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeWidth = (mOutRadio / 6).toFloat()
        paint
    }
    //2.3 错误的画笔
    private val mErrorPaint by lazy {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeWidth = (mOutRadio / 6).toFloat()
        paint
    }
    //2.4 正常的画笔
    private val mNormalPaint by lazy {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeWidth = (mOutRadio / 9).toFloat()
        paint
    }
    //2.5 箭头的画笔
    private val mArrowPaint by lazy {
        val paint = Paint()
        paint.color = mInnerPressedColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint
    }

    override fun onDraw(canvas: Canvas?) {
        //onDraw会调用多次
        if (!mIsInit) {
            mIsInit = true
            //确保只绘制一次
            //绘制9个点
            initDot()
        }

        //绘制9宫格
        drawShow(canvas!!)
    }

    private fun drawShow(canvas: Canvas) {
        for (i in 0..8) {
            for (point in mPoints[i]) {
                //先绘制外圆
                if (point == null) {
                    continue
                }
                //绘制外圆
                canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(), mOutRadio.toFloat(), mNormalPaint)

                //绘制内圆
                mNormalPaint.color = mInnerNormalColor
                canvas.drawCircle(
                    point.centerX.toFloat(), point.centerY.toFloat(),
                    (mOutRadio / 3).toFloat(), mNormalPaint
                )
            }
        }
    }

    private fun initDot() {
        //9宫格的点存放到 3x3数组中
        //不断绘制的时候这几个点都有状态，而且后面肯定需要回调密码点

        //获取当前宽度
        var nowWidth = width
        var nowHeight = height

        //水平偏移距离
        var offsetX = 0
        var offsetY = 0

        //y 的便宜距离其实就是 大的宽度减去小的高度 剩下的/2
        if (nowHeight > nowWidth) {
            //view往下移动
            offsetY = (nowHeight - nowWidth) / 2
            nowHeight = nowWidth
        } else {
            //view往左移动
            offsetX = (nowWidth - nowHeight) / 2
            nowWidth = nowHeight
        }
        //一个item 大小
        val oneWidth = nowWidth / 3

        //设置外圆大小
        mOutRadio = oneWidth / 2


        //9个点
        //算出9个点的坐标
        mPoints[0][0] = Point(offsetX + oneWidth / 2, offsetY + oneWidth / 2, 0)
        mPoints[0][1] = Point(offsetX + oneWidth * 3 / 2, offsetY + oneWidth / 2, 1)
        mPoints[0][2] = Point(offsetX + oneWidth * 5 / 2, offsetY + oneWidth / 2, 2)

        mPoints[1][0] = Point(offsetX + oneWidth / 2, offsetY + oneWidth * 3 / 2, 3)
        mPoints[1][1] = Point(offsetX + oneWidth * 3 / 2, offsetY + oneWidth * 3 / 2, 4)
        mPoints[1][2] = Point(offsetX + oneWidth * 5 / 2, offsetY + oneWidth * 3 / 2, 5)

        mPoints[2][0] = Point(offsetX + oneWidth / 2, offsetY + oneWidth * 5 / 2, 6)
        mPoints[2][1] = Point(offsetX + oneWidth * 3 / 2, offsetY + oneWidth * 5 / 2, 7)
        mPoints[2][2] = Point(offsetX + oneWidth * 5 / 2, offsetY + oneWidth * 5 / 2, 8)

    }

    enum class PointStatusEnum {
        NORMAL, PRESSED, ERROR
    }

    data class Point(
        var centerX: Int,
        var centerY: Int,
        /**
         * 下标
         */
        var index: Int,
        /**
         * 当前点的状态
         */
        var status: PointStatusEnum = PointStatusEnum.NORMAL
    )
}