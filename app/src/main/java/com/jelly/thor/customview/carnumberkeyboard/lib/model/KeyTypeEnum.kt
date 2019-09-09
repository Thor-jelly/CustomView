package com.jelly.thor.customview.carnumberkeyboard.lib.model

/**
 * 类描述：键盘类型<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/9/2 11:46 <br/>
 */
enum class KeyTypeEnum (val key: Int, val value: String) {
    TEXT(-1, "文本"),
    CN(-2, "中文"),
    ENG(-3, "ABC"),
    DEL(-4, "删除");
}