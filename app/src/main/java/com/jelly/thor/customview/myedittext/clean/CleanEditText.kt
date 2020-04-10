package com.jelly.thor.customview.myedittext.clean

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.jelly.thor.customview.R

/**
 * 类描述：右侧是清楚按钮的布局 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2020/4/10 16:56 <br/>
 */
class CleanEditText @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    AppCompatEditText(context, attrs, defStyleAttr) {
    private lateinit var mClearDrawable:Drawable

    init {
        val ta = context!!.obtainStyledAttributes(attrs, R.styleable.CleanEditText)
        mClearDrawable = ta.getDrawable(R.styleable.CleanEditText_cleanet_clean_ic) ?: context.resources.getDrawable(R.drawable.clean_edit_text_clean_ic)
        ta.recycle()
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        //具有焦点，并且文字不为空 显示清除按钮
        showClear(isFocusable && !text.isNullOrEmpty())
    }

    private fun showClear(visible: Boolean) {
        //设置显隐清除图标
        mClearDrawable.setBounds(0, 0, 16.dp2px(context), 16.dp2px(context))
        setCompoundDrawables(
            null,
            null,
            if (visible) mClearDrawable else null,
            null
        )
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)

        //具有焦点，并且文字不为空 显示清除按钮
        showClear(isFocusable && !text.isNullOrEmpty())
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_UP -> {
                    /*if (isClickClear(event.x)) {

                    }*/
                }
            }
        }
        return super.onTouchEvent(event)
    }
}

private fun Int.dp2px(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}