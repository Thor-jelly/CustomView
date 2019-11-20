package com.jelly.thor.customview.wxbook

import androidx.recyclerview.widget.RecyclerView
import com.github.promeg.pinyinhelper.Pinyin

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/11/20 09:46 <br/>
 */
abstract class WXBaseAdapter<T : WXBean>(val list: MutableList<T>) :
    RecyclerView.Adapter<WXViewHolder>() {
    init {
        //初始化Pinyin字段 并且排序
        for ((index, value) in list.withIndex()) {
            val zh2pinyin = value.zh2pinyin
            if (zh2pinyin.isEmpty()) {
                value.pinyin = "#"
            } else {
                val toUpperCase = Pinyin.toPinyin(zh2pinyin, "").toUpperCase()
                if (toUpperCase.matches("[A-Za-z].*".toRegex())) {
                    value.pinyin = toUpperCase
                } else {
                    value.pinyin = "#"
                }
            }
        }
        list.sort()
    }
}