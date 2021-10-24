package com.jelly.thor.customview.notchline

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.RelativeLayout
import com.jelly.thor.customview.R

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2021/10/24 17:39 <br/>
 */
class YeloWmsNotchLineView @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : RelativeLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val LINE = 1
        private const val COLOR = 2
    }

    //顶部title 圆角
    private var topRadius = dp2px(10)

    //顶部title 颜色填充样式；1：线性渐进线 2：纯色
    private var topFillColorStyle = LINE

    //开始颜色，或纯色模式设置的颜色
    private var colorStart = Color.YELLOW

    //结束颜色，只在渐进线模式有用
    private var colorEnd = Color.GREEN

    //bottom颜色
    private var bottomColor = Color.WHITE

    //缺口圆角半径
    private var notchRadius = dp2px(5)

    private lateinit var titlePaint: Paint
    private lateinit var bottomPaint: Paint

    init {
        if (context == null) {
            throw RuntimeException("YeloWmsNotchLineView context=null，请检查！")
        }
        val ob = context.obtainStyledAttributes(attrs, R.styleable.YeloWmsNotchLineView)
        topRadius =
            ob.getDimension(R.styleable.YeloWmsNotchLineView_topRadius, dp2px(10).toFloat()).toInt()
        topFillColorStyle = ob.getInt(R.styleable.YeloWmsNotchLineView_topFillColorStyle, LINE)
        colorStart = ob.getColor(R.styleable.YeloWmsNotchLineView_colorStart, Color.YELLOW)
        colorEnd = ob.getColor(R.styleable.YeloWmsNotchLineView_colorEnd, Color.GREEN)
        bottomColor = ob.getColor(R.styleable.YeloWmsNotchLineView_bottomColor, Color.WHITE)
        notchRadius =
            ob.getDimension(R.styleable.YeloWmsNotchLineView_topRadius, dp2px(5).toFloat()).toInt()

        ob.recycle()
        initBottomPaint()
    }

    override fun dispatchDraw(canvas: Canvas?) {
        initTitlePaint()
        drawTitle(canvas)
        drawBottom(canvas)
        super.dispatchDraw(canvas)
    }

    private fun drawBottom(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        val path = Path()
        path.moveTo(
            notchRadius.toFloat(),
            (height - notchRadius).toFloat()
        )
        path.lineTo(
            (width - notchRadius).toFloat(),
            (height - notchRadius).toFloat()
        )
        path.arcTo(
            RectF(
                (width - notchRadius).toFloat(),
                (height - notchRadius * 2).toFloat(),
                (width + notchRadius).toFloat(),
                height.toFloat() + 1F
            ),
            -180F,
            -90F
        )
        path.lineTo(
            0F,
            height.toFloat() + 1F
        )
        path.arcTo(
            RectF(
                (-notchRadius).toFloat(),
                (height - notchRadius * 2).toFloat(),
                (notchRadius).toFloat(),
                height.toFloat() + 1F
            ),
            90F,
            -90F
        )
        path.close()
        canvas.drawPath(path, bottomPaint)
    }

    private fun drawTitle(canvas: Canvas?) {
        if (canvas == null) {
            return
        }
        val path = Path()
        path.addArc(
            RectF(
                0F,
                0F,
                (topRadius * 2).toFloat(),
                (topRadius * 2).toFloat()
            ),
            -180F,
            90F
        )
        path.lineTo((width - topRadius).toFloat(), 0F)
        path.arcTo(
            RectF(
                (width - (topRadius * 2)).toFloat(),
                0F,
                width.toFloat(),
                (topRadius * 2).toFloat()
            ),
            -90F,
            90F
        )
        path.lineTo(
            width.toFloat(),
            (height - notchRadius * 2).toFloat()
        )
        path.arcTo(
            RectF(
                (width - notchRadius).toFloat(),
                (height - notchRadius * 2).toFloat(),
                (width + notchRadius).toFloat(),
                height.toFloat()
            ),
            -90F,
            -90F
        )
        path.lineTo(
            notchRadius.toFloat(),
            (height - notchRadius).toFloat()
        )
        path.arcTo(
            RectF(
                (-notchRadius).toFloat(),
                (height - notchRadius * 2).toFloat(),
                (notchRadius).toFloat(),
                height.toFloat()
            ),
            0F,
            -90F
        )
        path.close()
        canvas.drawPath(path, titlePaint)
    }

    //初始化title画笔
    private fun initTitlePaint() {
        if (::titlePaint.isInitialized) {
            return
        }
        titlePaint = Paint()
        titlePaint.isDither = true
        titlePaint.isAntiAlias = true
        //设置颜色
        if (LINE == topFillColorStyle) {
            titlePaint.shader = LinearGradient(
                0F,
                0F,
                measuredWidth.toFloat(),
                0F,
                colorStart,
                colorEnd,
                Shader.TileMode.CLAMP
            )
        } else {
            titlePaint.color = colorStart
        }
    }

    //初始化bottom画笔
    private fun initBottomPaint() {
        bottomPaint = Paint()
        bottomPaint.isAntiAlias = true
        bottomPaint.isDither = true
        bottomPaint.color = bottomColor
    }

    private fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
            .toInt()
    }
}