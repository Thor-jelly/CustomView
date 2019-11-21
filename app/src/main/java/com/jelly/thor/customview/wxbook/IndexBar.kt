package com.jelly.thor.customview.wxbook

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Vibrator
import android.util.AttributeSet
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import kotlin.math.max

/**
 * 类描述：索引view<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/20 11:49 <br/>
 */
class IndexBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private val allChar = arrayOf(
        "A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z",
        "#"
    )

    /**
     * 字体画笔
     */
    private val charPaint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint
    }

    /**
     * 选中背景画笔
     */
    private val radioPaint by lazy {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint
    }

    private var isPressDown = false

    private var listener: IndexBarListener? = null

    private var builder: Builder

    init {
        //暂时是在其他view中，不需要自定义属性
        //字体大小
        //字体颜色-未选中
        //字体颜色-已选中
        //字体已选中圆背景色
        //点击是否振动
        builder = Builder().setTextSize(dp2px(11))
    }

    /**
     * 设置属性
     */
    fun setBuilder(builder: Builder) {
        this.builder = builder
        invalidate()
    }

    fun setListener(listener: IndexBarListener) {
        this.listener = listener
    }

    fun setCurrentChar(currentChar: String) {
        if (isPressDown) {
            return
        }
        builder.setCurrentChar(currentChar)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        charPaint.textSize = builder.textSize.toFloat()
        var charAllWidthSize = 0
        var charAllHeightSize = 0
        //计算字体高度 宽度
        val charRect = Rect()
        for (s in allChar) {
            charPaint.getTextBounds(s, 0, s.length, charRect)
            charAllWidthSize = max(charRect.width(), charAllWidthSize)
            charAllHeightSize = max(charRect.height(), charAllHeightSize)
        }
        charAllHeightSize *= allChar.size

        val widthSize = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
            MeasureSpec.AT_MOST -> charAllWidthSize + paddingStart + paddingEnd
            else -> charAllWidthSize + paddingStart + paddingEnd
        }

        val heightSize = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST -> charAllHeightSize + dp2px(10) * (allChar.size - 1)
            else -> charAllHeightSize + dp2px(10) * (allChar.size - 1)
        }

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //top开始位置
        var startInt = paddingTop
        //字符高度
        val charHeight = height - paddingTop - paddingBottom
        val oneCharHeight = charHeight / allChar.size
        val charRect = Rect()
        for ((index, s) in allChar.withIndex()) {
            //字母中心
            val centerY = index * oneCharHeight + oneCharHeight / 2 + paddingTop
            //基线计算
            val fontMetrics = charPaint.fontMetrics
            val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
            val baseLine = centerY + dy;

            charPaint.getTextBounds(s, 0, s.length, charRect)
            if (s == builder.currentChar) {
                charPaint.color = builder.textSelectColor

                //绘制小圆
                radioPaint.color = builder.circleSelectColor
                val radio = charRect.width() / 2 + dp2px(5)
                canvas.drawCircle(
                    (width / 2).toFloat(),
                    centerY.toFloat(),
                    radio.toFloat(),
                    radioPaint
                )
            } else {
                charPaint.color = builder.textNoSelectColor
            }
            canvas.drawText(
                s,
                (width / 2 - charRect.width() / 2).toFloat(),
                baseLine,
                charPaint
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressDown = true
                val currentMoveY = event.y
                val oneCharHeight = (height - paddingTop - paddingBottom) / allChar.size
                var currentPosition = ((currentMoveY - paddingTop) / oneCharHeight).toInt()
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                if (currentPosition > allChar.size - 1) {
                    currentPosition = allChar.size - 1
                }
                val nowClickChar = allChar[currentPosition]
                if (builder.currentChar != nowClickChar) {
                    builder.setCurrentChar(nowClickChar)
                    invalidate()
                }

                val pointF =
                    PointF(
                        0F,
                        paddingTop + (currentPosition * oneCharHeight + oneCharHeight / 2).toFloat()
                    )
                listener?.touchListener(builder.currentChar, true, pointF)
                playVibrator()
            }
            MotionEvent.ACTION_MOVE -> {
                val currentMoveY = event.y
                val oneCharHeight = (height - paddingTop - paddingBottom) / allChar.size
                var currentPosition = ((currentMoveY - paddingTop) / oneCharHeight).toInt()
                if (currentPosition < 0) {
                    currentPosition = 0;
                }
                if (currentPosition > allChar.size - 1) {
                    currentPosition = allChar.size - 1
                }
                val nowClickChar = allChar[currentPosition]
                if (builder.currentChar != nowClickChar) {
                    builder.setCurrentChar(nowClickChar)
                    val pointF =
                        PointF(
                            0F,
                            paddingTop + (currentPosition * oneCharHeight + oneCharHeight / 2).toFloat()
                        )
                    listener?.touchListener(builder.currentChar, true, pointF)
                    playVibrator()
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL -> {
                isPressDown = false
                listener?.touchListener(builder.currentChar, false, null)
            }
            else -> {
            }
        }
        return true
    }

    private fun playVibrator() {
        if (builder.isVibrator) {
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
//            val vibrator =
//                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//            vibrator.vibrate(8L)
        }
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
            .toInt()
    }

    interface IndexBarListener {
        fun touchListener(str: String, isShowPreView: Boolean, pointF: PointF?)
    }

    class Builder {
        //字体大小
        var textSize = 11
            private set
        //字体颜色-已选中
        var textSelectColor = Color.GRAY
            private set
        //字体颜色-未选中
        var textNoSelectColor = Color.BLACK
            private set
        //字体已选中圆背景色
        var circleSelectColor = Color.RED
            private set
        //点击是否振动
        var isVibrator = true
            private set
        //当前选中字母
        var currentChar = ""
            private set

        fun setCurrentChar(currentChar: String): Builder {
            this.currentChar = currentChar
            return this
        }

        fun setTextSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        fun setTextSelectColor(textSelectColor: Int): Builder {
            this.textSelectColor = textSelectColor
            return this
        }

        fun setTextNoSelectColor(textNoSelectColor: Int): Builder {
            this.textNoSelectColor = textNoSelectColor
            return this
        }

        fun setCircleSelectColor(circleSelectColor: Int): Builder {
            this.circleSelectColor = circleSelectColor
            return this
        }

        fun setIsVibrator(isVibrator: Boolean): Builder {
            this.isVibrator = isVibrator
            return this
        }
    }
}