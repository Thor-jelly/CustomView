package com.jelly.thor.customview.equalscalecouponview

import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/20 19:59 <br/>
 */
/**
 * 当前缩放view必须用一个父布局包裹
 */
@Suppress("USELESS_ELVIS")
fun View?.scaleView(
    isFullView: Boolean = false
) {
    if (this == null) {
        return
    }

    val parentView =
        this.parent as ViewGroup ?: throw IllegalArgumentException("当前缩放view必须用一个父布局包裹")

    //放大view外侧view的父布局
    val parentPView = parentView.parent as ViewGroup

    val childViewHeight = this.measuredHeight
    val childViewWidth = this.measuredWidth

    val newXI = parentPView.width
    val newYI = newXI * childViewHeight / childViewWidth

    this.pivotX = 0f
    this.pivotY = 0f

    val scaleX = newXI / childViewWidth.toFloat()
    val scaleY = newYI / childViewHeight.toFloat()


    val setFullViewHeight: Int
    if (isFullView) {
        //放大view外层view在其父布局未知
        val parentPViewIndex = parentPView.indexOfChild(parentView)
        //放大view外层view 其父布局一共有多少子view
        val allParentChildCount = parentPView.childCount
        //不是放大view的高度
        var noScaleViewHeight = 0
        if (parentPViewIndex > 0) {
            for (i in 0 until allParentChildCount) {
                if (i == parentPViewIndex) {
                    continue
                }
                noScaleViewHeight += parentPView.getChildAt(i).height
            }
        }

        setFullViewHeight = parentPView.height - noScaleViewHeight

        val newChildViewHeight = setFullViewHeight / scaleY
        this.layoutParams.height = newChildViewHeight.roundToInt()
    } else {
        setFullViewHeight =  ViewGroup.LayoutParams.MATCH_PARENT
    }

    this.scaleX = scaleX
    this.scaleY = scaleY

    val layoutParams = parentView.layoutParams
    layoutParams.width = newXI
    layoutParams.height = if (isFullView) {
       setFullViewHeight
    } else {
        newYI
    }
    parentView.layoutParams = layoutParams
}