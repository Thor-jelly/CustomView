package com.jelly.thor.customview.ninelockpalaces

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
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

    private val mPoint: Point?
        get() {
            for (outPoint in mPoints) {
                for (inPoint in outPoint) {
                    if (inPoint == null) {
                        continue
                    }
                    val centerX = inPoint.centerX
                    val centerY = inPoint.centerY
                    if (NineLockPalaceUtils.checkInRound(
                            centerX.toFloat(),
                            centerY.toFloat(),
                            mOutRadio.toFloat(),
                            mMoveX.toFloat(),
                            mMoveY.toFloat()
                        )
                    ) {
                        //在圆内
                        return inPoint
                    }
                }
            }
            return null
        }

    /**
     * 移动x轴
     */
    private var mMoveX = 0F
    /**
     * 移动y轴
     */
    private var mMoveY = 0F
    /**
     * 是否按下的时候在一个点上
     */
    private var mIsTouchPoint = false

    /**
     * 按下的点集合
     */
    private val mSelectPointsList = mutableListOf<Point>()

    /**
     * 保存的点的集合 todo 具体操作就不加了，我这边默认给 外圈7
     */
    private var mSavePointIndexList = mutableListOf<Int>(0, 1, 2, 5, 8)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action ?: return super.onTouchEvent(event)
        mMoveX = event.x
        mMoveY = event.y
        //处理触摸事件
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                //按下
                if (mSelectPointsList.isNotEmpty()) {
                    for (point in mSelectPointsList) {
                        point.setStatusNormal()
                    }
                    mSelectPointsList.clear()
                    invalidate()
                }

                mIsTouchPoint = true
                //1.判断手指是否在圆内，点到圆心的距离 <= 半径
                //mPoint 中get方法
                val point = mPoint
                if (point != null) {
                    mSelectPointsList.add(point)
                    //2.改变当前点的状态
                    point.setStatusPressed()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                //移动
                if (mIsTouchPoint) {
                    //按下的时候一定要在一个点上，不断移动不断判断新的点
                    val point = mPoint
                    if (point != null) {
                        if (!mSelectPointsList.contains(point)) {
                            //没有点的时候再添加
                            mSelectPointsList.add(point)
                        }
                        //2.改变当前点的状态
                        point.setStatusPressed()
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                //抬起
                mIsTouchPoint = false
                //回调密码，如果错误显示错误完要清空
                if (mSavePointIndexList.size != mSelectPointsList.size) {
                    for (point in mSelectPointsList) {
                        point.setStatusError()
                    }
                } else {
                    //顺序需要判断
                    for (index in mSavePointIndexList.indices) {
                        val saveIndex = mSavePointIndexList[index]
                        val point = mSelectPointsList[index]
                        if(point.index != saveIndex){
                            //下标不一致，错误
                            for (aPoint in mSelectPointsList) {
                                aPoint.setStatusError()
                            }
                        }
                    }
                }
            }
            else -> {
            }
        }
        invalidate()
        return true
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
        //绘制两个点之间连线
        drawLine(canvas)
    }

    /**
     * 绘制两个点之间连线
     */
    private fun drawLine(canvas: Canvas) {
        if (mSelectPointsList.isEmpty()) {
            //如果没有点则返回
            return
        }
        //1.获取当前所有保存的连线
        var lastPoint = mSelectPointsList[0]
        for (i in 1 until mSelectPointsList.size) {
            //绘制直线
            val newLastPoint = mSelectPointsList[i]

            mLinePaint.color = when {
                lastPoint.statusIsNormal() || lastPoint.statusIsPressed() -> mInnerPressedColor
                lastPoint.statusIsError() -> mInnerErrorColor
                else -> mInnerPressedColor
            }

            drawLine(lastPoint, newLastPoint, canvas, mLinePaint)

            //两个点之间绘制箭头
            drawArrow(lastPoint, newLastPoint, canvas, mArrowPaint)

            //赋值最新的点 到 变量上
            lastPoint = newLastPoint
        }

        //2.获取最后一个点到手点位置的连线
        //内圆半径， 需要跟绘制内圆的保持一致
        val inRadio = mOutRadio / 6
        val checkInRound =
            NineLockPalaceUtils.checkInRound(
                lastPoint.centerX.toFloat(),
                lastPoint.centerY.toFloat(),
                inRadio.toFloat(),
                mMoveX,
                mMoveY
            )
        //如果在内圆内 并且 手指已经离开 并且 已经全部选中9宫格 不需要画线
        if (mIsTouchPoint && !checkInRound && mSelectPointsList.size < 9) {
            mLinePaint.color = when {
                lastPoint.statusIsNormal() || lastPoint.statusIsPressed() -> mInnerPressedColor
                lastPoint.statusIsError() -> mInnerErrorColor
                else -> mInnerPressedColor
            }

            drawLine(lastPoint, Point(mMoveX.toInt(), mMoveY.toInt(), -1), canvas, mLinePaint)
        }
    }

    /**
     * 绘制三角箭头
     */
    @Suppress("UNUSED_PARAMETER")
    private fun drawArrow(
        lastPoint: Point,
        newLastPoint: Point,
        canvas: Canvas,
        mArrowPaint: Paint
    ) {

    }

    /**
     * 绘制直线
     */
    private fun drawLine(
        startPoint: NineLockPalacesView.Point,
        endPoint: NineLockPalacesView.Point,
        canvas: Canvas,
        paint: Paint
    ) {
        //1.获取两个点之间距离
        val get2PointDistance = NineLockPalaceUtils.get2PointDistance(
            startPoint.centerX.toFloat(),
            startPoint.centerY.toFloat(),
            endPoint.centerX.toFloat(),
            endPoint.centerY.toFloat()
        )
        //内圆半径， 需要跟绘制内圆的保持一致
        val inRadio = mOutRadio / 6
        val xValue = endPoint.centerX.toFloat() - startPoint.centerX.toFloat()
        val yValue = endPoint.centerY.toFloat() - startPoint.centerY.toFloat()
        // d/ir = xv/dx
        val dx = xValue * inRadio / get2PointDistance
        // d/ir = yv/dy
        val dy = yValue * inRadio / get2PointDistance

        //画线
        canvas.drawLine(
            startPoint.centerX + dx,
            startPoint.centerY + dy,
            endPoint.centerX - dx,
            endPoint.centerY - dy,
            paint
        )
    }

    @Suppress("SENSELESS_COMPARISON")
    private fun drawShow(canvas: Canvas) {
        for (outPoint in mPoints) {
            if (outPoint == null) {
                continue
            }
            for (intPoint in outPoint) {
                //先绘制外圆
                if (intPoint == null) {
                    continue
                }
                //绘制外圆
                val newPaint = when {
                    intPoint.statusIsNormal() -> mNormalPaint
                    intPoint.statusIsError() -> mErrorPaint
                    intPoint.statusIsPressed() -> mPressedPaint
                    else -> mNormalPaint
                }

                newPaint.color = when {
                    intPoint.statusIsNormal() -> mOuterNormalColor
                    intPoint.statusIsError() -> mOuterErrorColor
                    intPoint.statusIsPressed() -> mOuterPressedColor
                    else -> mOuterNormalColor
                }
                canvas.drawCircle(
                    intPoint.centerX.toFloat(),
                    intPoint.centerY.toFloat(),
                    mOutRadio.toFloat(),
                    newPaint
                )

                //绘制内圆
                newPaint.color = when {
                    intPoint == null -> mInnerNormalColor
                    intPoint.statusIsNormal() -> mInnerNormalColor
                    intPoint.statusIsError() -> mInnerErrorColor
                    intPoint.statusIsPressed() -> mInnerPressedColor
                    else -> mInnerNormalColor
                }
                canvas.drawCircle(
                    intPoint.centerX.toFloat(),
                    intPoint.centerY.toFloat(),
                    (mOutRadio / 6).toFloat(),
                    newPaint
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
        mOutRadio = oneWidth / 4


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
    ) {
        fun setStatusPressed() {
            status = PointStatusEnum.PRESSED
        }

        fun setStatusNormal() {
            status = PointStatusEnum.NORMAL
        }

        fun setStatusError() {
            status = PointStatusEnum.ERROR
        }

        fun statusIsPressed(): Boolean {
            return status == PointStatusEnum.PRESSED
        }

        fun statusIsNormal(): Boolean {
            return status == PointStatusEnum.NORMAL
        }

        fun statusIsError(): Boolean {
            return status == PointStatusEnum.ERROR
        }
    }
}