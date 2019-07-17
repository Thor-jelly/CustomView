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
        //x值
        val lineX = moveX - x
        //y值
        val lineY = moveY - y
        //移动的半径值
        val lineR2 = lineX.pow(2) + lineY.pow(2)
        val moveR = sqrt(lineR2)
        return moveR <= r
    }
}