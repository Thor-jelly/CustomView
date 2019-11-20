package com.jelly.thor.customview.wxbook

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
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
     * 字体大小
     */
    private var textSize = dp2px(11)
    /**
     * 字体颜色-未选中
     */
    private var noSelectTextColor = Color.BLACK
    /**
     * 字体颜色-已选中
     */
    private var selectTextColor = Color.GRAY

    /**
     * 圆
     */
    private var radioColor = Color.RED

    /**
     * 当前字母
     */
    private var currentChar = ""

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

    private var listener: IndexBarListener? = null

    init {
        //字体大小
        //字体颜色-未选中
        //字体颜色-已选中
        //字体已选中背景
        //整体背景
        //点击是否振动
    }

    fun setListener(listener: IndexBarListener) {
        this.listener = listener
    }

    @JvmOverloads
    fun setCurrentChar(currentChar: String) {
        this.currentChar = currentChar
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        charPaint.textSize = textSize.toFloat()
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
            if (s == currentChar) {
                charPaint.color = selectTextColor

                //绘制小圆
                radioPaint.color = radioColor
                val radio = charRect.width() / 2 + dp2px(5)
                canvas.drawCircle(
                    (width / 2).toFloat(),
                    centerY.toFloat(),
                    radio.toFloat(),
                    radioPaint
                )
            } else {
                charPaint.color = noSelectTextColor
            }
            canvas.drawText(
                s,
                (width / 2 - charRect.width() / 2).toFloat(),
                baseLine,
                charPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
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
                if (currentChar != nowClickChar) {
                    currentChar = nowClickChar
                    invalidate()
                }
                val pointF =
                    PointF(
                        0F,
                        paddingTop + (currentPosition * oneCharHeight + oneCharHeight / 2).toFloat()
                    )

                listener?.touchListener(currentChar, true, pointF)
            }
            MotionEvent.ACTION_UP -> {
                listener?.touchListener(currentChar, false, null)
            }
            else -> {
            }
        }
        return true
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
}