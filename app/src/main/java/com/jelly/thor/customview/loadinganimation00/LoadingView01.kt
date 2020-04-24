package com.jelly.thor.customview.loadinganimation00

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.jelly.thor.customview.R

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

    private val initStartX
        get() = 0 + pointRadio

    private val initEndX
        get() = width - pointRadio

    private val duration = 500L

    init {
        //初始化完，执行动画
        post {
            startAnimator()
        }
    }

    /**
     * 开启动画
     */
    fun startAnimator() {
        //开始往里缩动画
        val oneAnimator = ValueAnimator.ofFloat(oneX.toFloat(), twoX.toFloat())
        oneAnimator.addUpdateListener {
            oneX = it.animatedValue.toString().toFloat().toInt()
        }

        val threeAnimator = ValueAnimator.ofFloat(threeX.toFloat(), twoX.toFloat())
        threeAnimator.addUpdateListener {
            threeX = it.animatedValue.toString().toFloat().toInt()
            invalidate()
        }

        val set = AnimatorSet()
        set.duration = duration
        set.interpolator = AccelerateInterpolator()
        set.playTogether(oneAnimator, threeAnimator)
        set.start()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                changPoint(onePaint)
                changPoint(twoPaint)
                changPoint(threePaint)
                starAnimatorOut()
            }
        })
    }

    private fun starAnimatorOut() {
        //开始往外扩张动画
        //开始往里缩动画
        val oneAnimator = ValueAnimator.ofFloat(twoX.toFloat(), initStartX.toFloat())
        oneAnimator.addUpdateListener {
            oneX = it.animatedValue.toString().toFloat().toInt()
        }

        val threeAnimator = ValueAnimator.ofFloat(twoX.toFloat(), initEndX.toFloat())
        threeAnimator.addUpdateListener {
            threeX = it.animatedValue.toString().toFloat().toInt()
            invalidate()
        }

        val set = AnimatorSet()
        set.duration = duration
        set.interpolator = DecelerateInterpolator()
        set.playTogether(oneAnimator, threeAnimator)
        set.start()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                startAnimator()
            }
        })
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clearAnimation()
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
            oneX = initStartX
            twoX = width / 2
            threeX = initEndX
        }

        canvas.drawCircle(oneX.toFloat(), currentY.toFloat(), pointRadio.toFloat(), onePaint)
        canvas.drawCircle(threeX.toFloat(), currentY.toFloat(), pointRadio.toFloat(), threePaint)
        canvas.drawCircle(twoX.toFloat(), currentY.toFloat(), pointRadio.toFloat(), twoPaint)
    }

    private fun changPoint(currentPaint: Paint) {
        val nowColor = when (currentPaint.color) {
            oneColor -> threeColor
            twoColor -> oneColor
            threeColor -> twoColor
            else -> oneColor
        }
        currentPaint.color = nowColor
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}