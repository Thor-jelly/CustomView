package com.jelly.thor.customview.bezier

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

/**
 * 仿qq消息拖拽效果
 */
class Bezier01 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    companion object {
        /**
         * 没有点
         */
        const val NULL_POINT = -10000F
    }

    private val mPaint by lazy {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
        paint.isDither = true
        paint.isAntiAlias = true
        paint
    }

    /**
     * 固定点最小半径
     */
    private val mDownPointMiniRadius = dp2Px(3)

    /**
     * 固定圆
     */
    private val mDownPointRadius = dp2Px(10)
    /**
     * 拖拽圆
     */
    private val mMovePointRadius = dp2Px(15)

    private var mDownPoint: PointF = PointF(NULL_POINT, NULL_POINT)
    private var mMovePoint: PointF = PointF(NULL_POINT, NULL_POINT)

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //记录当前按下位置
                updatePoint(mDownPoint, event.x, event.y)
                updatePoint(mMovePoint, event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                updatePoint(mMovePoint, event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                updatePoint(mDownPoint, NULL_POINT, NULL_POINT)
                updatePoint(mMovePoint, NULL_POINT, NULL_POINT)
            }
        }
        invalidate()
        return true
    }

    private fun dp2Px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )
            .toInt()
    }

    /**
     * 更新点的位置
     */
    private fun updatePoint(pointF: PointF, x: Float, y: Float) {
        pointF.x = x
        pointF.y = y
    }

    override fun onDraw(canvas: Canvas) {
        if (mDownPoint.x == NULL_POINT) {
            return
        }
        //计算两个点的距离
        val new2PointDistance = calculation2PointDistance()

        //1、画拖拽圆
        canvas.drawCircle(mMovePoint.x, mMovePoint.y, mMovePointRadius.toFloat(), mPaint)

        val bezierPath = getBezierPath()
        bezierPath?.let {
            //2、画固定圆
            canvas.drawCircle(
                mDownPoint.x,
                mDownPoint.y,
                mDownPointRadius.toFloat() - new2PointDistance / 14,
                mPaint
            )
            canvas.drawPath(it, mPaint)
        }
    }

    /**
     * 计算两个点的距离
     */
    private fun calculation2PointDistance(): Float {
        val newX = abs(mDownPoint.x - mMovePoint.x)
        val newY = abs(mDownPoint.y - mMovePoint.y)
        return sqrt(newX.pow(2) + newY.pow(2))
    }


    /**
     * 获取贝塞尔路径
     * 具体看图片1(.customview\bezier\1.png)
     */
    private fun getBezierPath(): Path? {
        //判断固定点是否隐藏，如果隐藏不需要画贝塞尔曲线
        //计算两个点的距离d
        val c12c2distance = calculation2PointDistance()
        val nowDownPointRadius = mDownPointRadius.toFloat() - c12c2distance / 14
        val isDrawDownPoint = nowDownPointRadius > mDownPointMiniRadius
        val path = Path()
        if (!isDrawDownPoint) {
            //表示只需要画移动圆，固定圆和贝塞尔不需要画
            return null
        }

        //列出已知字段
        //两个圆距离 c12c2distance
        //两个圆中心位置
        val c1Point = mDownPoint
        val c2Point = mMovePoint
        //两个圆半径
        val c1R = nowDownPointRadius
        val c2R = mMovePointRadius

        val tanA = (c2Point.y - c1Point.y) / (c2Point.x - c1Point.x)
        //求角a
        val arcTanA = atan(tanA)
        //求p0 位置
        val p0PointX = c1Point.x + c1R * sin(arcTanA)
        val p0PointY = c1Point.y - c1R * cos(arcTanA)
        //求p2位置
        val p2PointX = c1Point.x - c1R * sin(arcTanA)
        val p2PointY = c1Point.y + c1R * cos(arcTanA)
        //求p1 位置
        val p1PointX = c2Point.x + c2R * sin(arcTanA)
        val p1PointY = c2Point.y - c2R * cos(arcTanA)
        //求p3位置
        val p3PointX = c2Point.x - c2R * sin(arcTanA)
        val p3PointY = c2Point.y + c2R * cos(arcTanA)

        //整合路径
        path.moveTo(p0PointX, p0PointY)
        //控制点 选择距离中心点 也可以黄金比例0.618
        val centerPointX = (c2Point.x + c1Point.x) / 2
        val centerPointY = (c2Point.y + c1Point.y) / 2
        path.quadTo(centerPointX, centerPointY, p1PointX, p1PointY)
        path.lineTo(p3PointX, p3PointY)
        path.quadTo(centerPointX, centerPointY, p2PointX, p2PointY)
        path.close()
        return path
    }
}