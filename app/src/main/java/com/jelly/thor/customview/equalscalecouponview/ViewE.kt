package com.jelly.thor.customview.equalscalecouponview

import android.graphics.Point
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import kotlin.math.roundToInt

/**
 * 类描述：//TODO:(这里用一句话描述这个方法的作用)    <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/20 19:59 <br/>
 */
/**
 * 当前缩放view必须用一个父布局包裹
 */
fun View?.scaleView(
    isFullView: Boolean = false
) {
    if (this == null) {
        return
    }

    val parentView =
        this.parent as ViewGroup ?: throw IllegalArgumentException("当前缩放view必须用一个父布局包裹")

    //生成testCl view截图
    val size = Point()
    ((this.context) as AppCompatActivity).window.windowManager.defaultDisplay.getSize(size)

    val childViewHeight = this.measuredHeight
    val childViewWidth = this.measuredWidth

    val newXI = size.x
    val newYI = newXI * childViewHeight / childViewWidth

    this.pivotX = 0f
    this.pivotY = 0f

    val scaleX = newXI / childViewWidth.toFloat()
    val scaleY = newYI / childViewHeight.toFloat()

    if (isFullView) {
        val parentPView = parentView.parent as ViewGroup
        val parentPViewIndex = parentPView.indexOfChild(parentView)

        var parentLayoutTopDistance = 0
        if (parentPViewIndex > 0) {
            for (i in 0 until parentPViewIndex) {
                parentLayoutTopDistance += parentPView.getChildAt(i).height
            }
        }

        val newChildViewHeight = (size.y - parentView.top) / scaleY
        this.layoutParams.height = newChildViewHeight.roundToInt()
    }

    this.scaleX = scaleX
    this.scaleY = scaleY

    val layoutParams = parentView.layoutParams
    layoutParams.width = newXI
    layoutParams.height = if (isFullView) ViewGroup.LayoutParams.MATCH_PARENT else newYI
    parentView.layoutParams = layoutParams
}