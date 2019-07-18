package com.jelly.thor.customview.ninelockpalaces

import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/7/17 18:49 <br/>
 */
object NineLockPalaceUtils {
    /**
     * 检测是否在圆内
     */
    fun checkInRound(x: Float, y: Float, r: Float, moveX: Float, moveY: Float): Boolean {
        val moveR = get2PointDistance(x, y, moveX, moveY)
        return moveR <= r
    }

    /**
     * 获取两个点之间距离
     */
    fun get2PointDistance(startX: Float, startY: Float, endX: Float, endY: Float): Float {
        val lineX = endX - startX
        val lineY = endY - startY
        val lineR2 = lineX.pow(2) + lineY.pow(2)
        return sqrt(lineR2)
    }
}