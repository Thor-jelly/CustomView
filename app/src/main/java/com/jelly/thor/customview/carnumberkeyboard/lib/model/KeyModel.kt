package com.jelly.thor.customview.carnumberkeyboard.lib.model

/**
 * 类描述：按键model<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/2 11:34 <br/>
 */
data class KeyModel(
    val id: String,
    val value: String = id,
    val icon: Int = NO_ICON,
    /**
     * 功能按键，看枚举中定义的
     */
    val keyTypeEnum: KeyTypeEnum = KeyTypeEnum.TEXT
) {
    companion object {
        const val NO_ICON = -1
    }
}