package com.jelly.thor.customview.loadinganimation00

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class LoadingView01 @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    /**
     * 颜色1
     */
    private val oneColor by lazy {
        Color.RED
    }

    /**
     * 颜色2
     */
    private val twoColor by lazy {
        Color.GREEN
    }

    /**
     * 颜色3
     */
    private val threeColor by lazy {
        Color.BLUE
    }

    /**
     * 小圆半径
     */
    private val pointRadio by lazy {
        dp2px(8)
    }

    /**
     * 小圆之间间距
     */
    private val pointDistance by lazy {
        dp2px(30)
    }

    private val onePaint by lazy {
        val paint = Paint()
        paint.color = oneColor
        paint.isAntiAlias = true
        paint.isDither = true
        paint
    }
    private val twoPaint by lazy {
        val paint = Paint()
        paint.color = twoColor
        paint.isAntiAlias = true
        paint.isDither = true
        paint
    }
    private val threePaint by lazy {
        val paint = Paint()
        paint.color = threeColor
        paint.isAntiAlias = true
        paint.isDither = true
        paint
    }

    private var oneX = 0
    private var twoX = 0
    private var threeX = 0
    private val currentY by lazy {
        height / 2
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST -> {
                pointRadio * 2 + paddingTop + paddingBottom
            }
            MeasureSpec.UNSPECIFIED -> {
                pointRadio * 2 + paddingTop + paddingBottom
            }
            MeasureSpec.EXACTLY -> {
               MeasureSpec.getSize(heightMeasureSpec)
            }
            else -> {
                pointRadio * 2 + paddingTop + paddingBottom
            }
        }
        val width = 3 * pointRadio * 2 + 2 * pointDistance + paddingStart + paddingEnd
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画小圆点
        if (twoX == 0) {
            oneX = 0 + pointRadio
            twoX = width / 2
            threeX = width - pointRadio
        }

        canvas.drawCircle(oneX.toFloat(), currentY.toFloat(), pointRadio.toFloat(), onePaint)
        canvas.drawCircle(twoX.toFloat(), currentY.toFloat(), pointRadio.toFloat(), twoPaint)
        canvas.drawCircle(threeX.toFloat(), currentY.toFloat(), pointRadio.toFloat(), threePaint)
    }

    private fun changPoint(currentColor: Int): Int {
        return when (currentColor) {
            oneColor -> twoColor
            twoColor -> threeColor
            threeColor -> oneColor
            else -> oneColor
        }
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}