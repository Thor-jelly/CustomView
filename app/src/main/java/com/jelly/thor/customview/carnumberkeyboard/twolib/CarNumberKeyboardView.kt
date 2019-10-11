package com.jelly.thor.customview.carnumberkeyboard.twolib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.util.AttributeSet
import com.jelly.thor.customview.R

/**
 * 类描述：自定义键盘样式 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/10/10 11:34 <br/>
 */
class CarNumberKeyboardView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) :
    KeyboardView(context, attrs) {

    private val mPaint by lazy {
        val paint = Paint()
        //通过反射获取字体大小
        var labelTextSize = 0
        try {
            val field =
                KeyboardView::class.java.getDeclaredField("mLabelTextSize")
            field.isAccessible = true
            labelTextSize = field.get(this) as Int
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        paint.textSize = labelTextSize.toFloat()

        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        paint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val keys = keyboard.keys
        for (key in keys) {
            if (key.codes[0] == Keyboard.KEYCODE_DONE
            ) {
                //重新绘制确定按钮样式,在原来布局上盖一层
                drawKeyStyle(canvas, key)
            }
        }
    }

    /**
     * 重新绘制确定按钮样式
     */
    private fun drawKeyStyle(canvas: Canvas, key: Keyboard.Key) {
        val drawable =
            context.resources.getDrawable(R.drawable.bg_car_number_keyboardview_preview_ensure)
        val state = key.currentDrawableState
        drawable.state = state
        drawable.setBounds(
            key.x + paddingStart,
            key.y + paddingTop,
            key.x + key.width + paddingStart,
            key.y + key.height + paddingTop
        )
        drawable.draw(canvas)

        if (key.label != null) {
            val label = key.label.toString()
            if (label.length > 1 && key.codes.size < 2) {
                mPaint.typeface = Typeface.DEFAULT_BOLD
            } else {
                mPaint.typeface = Typeface.DEFAULT
            }

            canvas.drawText(
                key.label.toString(),
                paddingStart + key.x + key.width / 2F,
                paddingTop + key.y + key.height / 2F
                        + (mPaint.textSize - mPaint.descent()) / 2F,
                mPaint
            )
        } else if (key.icon != null) {
            val icon = key.icon
            icon.setBounds(
                paddingStart + key.x + (key.width - icon.intrinsicWidth) / 2,
                paddingTop + key.y + (key.height - icon.intrinsicHeight) / 2,
                paddingStart + key.x + (key.width + icon.intrinsicWidth) / 2,
                paddingTop + key.y + (key.height + icon.intrinsicHeight) / 2
            )
            icon.draw(canvas)
        }
    }
}