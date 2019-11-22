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
        WXUtils.sort(list)
    }
}