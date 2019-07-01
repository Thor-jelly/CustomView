package com.jelly.thor.customview.utils

import android.graphics.Typeface
import android.widget.TextView
import com.jelly.thor.customview.App

/**
 * 类描述：TextView扩展方法<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/6/24 17:40 <br/>
 */
enum class FontStyleEnum {
    DIN_ALTERNATE_BOLD
}

private val DIN_ALTERNATE_BOLD = Typeface.createFromAsset(App.mApplication.assets, "fonts/DINAlternateBold.ttf")
    ?: throw IllegalArgumentException("检查字体保存路径是否有：fonts/DINAlternateBold.ttf")

/**
 * 设置字体样式
 */
fun TextView?.setFontStyle(fontStyle: FontStyleEnum) {
    if (this == null) {
        return
    }
    val typeface = when (fontStyle) {
        FontStyleEnum.DIN_ALTERNATE_BOLD -> DIN_ALTERNATE_BOLD
    }
    this.typeface = typeface
}